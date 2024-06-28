package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.ide.CopyPasteManager
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.foundation.ClearAction
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.IntellijEditorTextField
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.dnd.rememberDragAndDropHandler
import io.github.composegears.valkyrie.ui.foundation.icons.Collections
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.datatransfer.StringSelection
import java.io.File

val IconPackConversionScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = koinTiamatViewModel<IconPackConversionViewModel>()
    val state by viewModel.state.collectAsState()
    val settings by viewModel.valkyriesSettings.collectAsState()

    ConversionUi(
        state = state,
        settings = settings,
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
    state: IconPackConversionState,
    settings: ValkyriesSettings,
    onSelectFile: (File) -> Unit,
    openSettings: () -> Unit,
    resetIconContent: () -> Unit,
    onSelectNestedPack: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var showFilePicker by rememberMutableState { false }

    val project = LocalProject.current
    PluginUI(
        content = state.iconContent,
        settings = settings,
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
        openSettings = openSettings,
        onSelectFile = onSelectFile
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
    onChooseFile: () -> Unit,
    onClear: () -> Unit,
    onCopy: () -> Unit,
    onSelectPack: (String) -> Unit,
    onSelectFile: (File) -> Unit,
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
            if (settings.mode == Mode.IconPack && settings.nestedPacks.isNotEmpty()) {
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
                    onSelectFile = onSelectFile,
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
    var dropdownVisible by rememberMutableState { false }

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

@Composable
private fun SelectableState(
    onChooseFile: () -> Unit,
    onSelectFile: (File) -> Unit
) {
    val dragAndDropHandler = rememberDragAndDropHandler(onDrop = onSelectFile)
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) { dragAndDropHandler.isDragging }

    DragAndDropBox(
        isDragging = isDragging,
        onChoose = onChooseFile
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