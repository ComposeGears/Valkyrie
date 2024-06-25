package io.github.composegears.valkyrie.ui.screen.conversion

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.NavDestination
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.ide.CopyPasteManager
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.theme.LocalProject
import io.github.composegears.valkyrie.ui.foundation.ClearAction
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.IntellijEditorTextField
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.dashedBorder
import io.github.composegears.valkyrie.ui.foundation.icons.Collections
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.rememberDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.intro.Mode
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.datatransfer.StringSelection
import java.io.File

val ConversionScreen: NavDestination<Unit> by navDestination {
    val navController = navController()

    val viewModel = koinTiamatViewModel<ConversionViewModel>()
    val state by viewModel.state.collectAsState()
    val settings by viewModel.valkyriesSettings.collectAsState()

    val dragAndDropHandler = rememberDragAndDropHandler {
        viewModel.selectFile(it)
    }

    val isDragging by remember(dragAndDropHandler.isDragging) { mutableStateOf(dragAndDropHandler.isDragging) }

    ConversionUi(
        state = state,
        settings = settings,
        isDragging = isDragging,
        onSelectFile = {
            viewModel.selectFile(it)
            viewModel.updateLastChoosePath(it)
        },
        openSettings = {
            navController.navigate(
                dest = SettingsScreen,
                transition = navigationSlideInOut(true)
            )
        },
        resetIconContent = viewModel::reset,
        onSelectNestedPack = viewModel::selectNestedPack
    )
}

@Composable
private fun ConversionUi(
    state: ConversionState,
    settings: ValkyriesSettings,
    isDragging: Boolean,
    onSelectFile: (File) -> Unit,
    openSettings: () -> Unit,
    resetIconContent: () -> Unit,
    onSelectNestedPack: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var showFilePicker by remember { mutableStateOf(false) }

    val project = LocalProject.current
    PluginUI(
        content = state.iconContent,
        settings = settings,
        isDragging = isDragging,
        onChooseFile = { showFilePicker = true },
        onClear = resetIconContent,
        onCopy = {
            val text = state.iconContent ?: return@PluginUI
            CopyPasteManager.getInstance().setContents(StringSelection(text))

            scope.launch {
                val notification = NotificationGroupManager.getInstance()
                    .getNotificationGroup(/* groupId = */ "valkyrie")
                    .createNotification(content = "Copied in clipboard", type = NotificationType.INFORMATION)
                notification.notify(project)

                delay(2000)
                notification.expire()
            }
        },
        onSelectPack = onSelectNestedPack,
        openSettings = openSettings
    )

    FilePicker(
        show = showFilePicker,
        fileExtensions = listOf("svg", "xml"),
        initialDirectory = settings.initialDirectory,
        onFileSelected = { mpFile ->
            if (mpFile != null) {
                onSelectFile(File(mpFile.path))
                showFilePicker = false
            } else {
                showFilePicker = false
            }
        }
    )
}

@Composable
private fun PluginUI(
    content: String?,
    settings: ValkyriesSettings,
    isDragging: Boolean,
    onChooseFile: () -> Unit,
    onClear: () -> Unit,
    onCopy: () -> Unit,
    onSelectPack: (String) -> Unit,
    openSettings: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            if (content != null) {
                ClearAction(onClear)
                CopyAction(onCopy)
            }
            WeightSpacer()
            SettingsAction(openSettings = openSettings)
        }
        if (content != null) {
            if (settings.mode == Mode.IconPack) {
                NestedPacksDropdown(
                    settings = settings,
                    onSelectPack = onSelectPack
                )
            }
        }
        if (content != null) {
            IntellijEditorTextField(
                modifier = Modifier.fillMaxSize(),
                text = content
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SelectableState(
                    isDragging = isDragging,
                    onChooseFile = onChooseFile
                )
            }
        }
    }
}

@Composable
private fun NestedPacksDropdown(
    settings: ValkyriesSettings,
    onSelectPack: (String) -> Unit,
) {
    var dropdownVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(start = 12.dp, bottom = 16.dp)) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { dropdownVisible = true }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val rotation by animateFloatAsState(if (dropdownVisible) -180f else 0f)
            Text(
                text = "${settings.iconPackName}.${settings.currentNestedPack}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
            Icon(
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                },
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = dropdownVisible,
            onDismissRequest = { dropdownVisible = false }
        ) {
            settings.nestedPacks.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it)
                    },
                    onClick = {
                        dropdownVisible = false
                        onSelectPack(it)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SelectableState(
    isDragging: Boolean,
    onChooseFile: () -> Unit
) {
    var isHover by remember(isDragging) { mutableStateOf(isDragging) }

    val dashColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val border by animateDpAsState(if (isHover) 4.dp else 1.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.small)
            .onPointerEvent(PointerEventType.Enter) { isHover = true }
            .onPointerEvent(PointerEventType.Exit) { isHover = false }
            .dashedBorder(
                strokeWidth = border,
                gapWidth = 8.dp,
                dashWidth = 8.dp,
                color = dashColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(2.dp)
            .background(
                color = when {
                    isHover -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    else -> Color.Transparent
                },
                shape = MaterialTheme.shapes.small
            )
            .clickable(
                onClick = onChooseFile,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = ValkyrieIcons.Collections,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Drag & Drop or browse",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Supports: SVG, XML",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}