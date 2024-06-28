package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import io.github.composegears.valkyrie.ui.foundation.*
import io.github.composegears.valkyrie.ui.foundation.dnd.rememberDragAndDropHandler
import io.github.composegears.valkyrie.ui.foundation.icons.Collections
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.datatransfer.StringSelection
import java.io.File

val SimpleConversionScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = koinTiamatViewModel<SimpleConversionViewModel>()
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
        resetIconContent = viewModel::reset
    )
}

@Composable
private fun ConversionUi(
    state: SimpleConversionState,
    settings: ValkyriesSettings,
    onSelectFile: (File) -> Unit,
    openSettings: () -> Unit,
    resetIconContent: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var showFilePicker by rememberMutableState { false }

    val project = LocalProject.current
    PluginUI(
        content = state.iconContent,
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
        onSelectFile = onSelectFile,
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
    onChooseFile: () -> Unit,
    onClear: () -> Unit,
    onCopy: () -> Unit,
    onSelectFile: (File) -> Unit,
    openSettings: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            AppBarTitle(title = "Simple conversion")
            WeightSpacer()
            if (content != null) {
                ClearAction(onClear)
                CopyAction(onCopy)
            }
            SettingsAction(openSettings = openSettings)
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