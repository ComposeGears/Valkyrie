package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.ide.CopyPasteManager
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.ClearAction
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.IntellijEditorTextField
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.dnd.rememberFileDragAndDropHandler
import io.github.composegears.valkyrie.ui.foundation.icons.Collections
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.picker.rememberFilePicker
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
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

    ConversionUi(
        state = state,
        onSelectFile = viewModel::selectFile,
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
    onSelectFile: (File) -> Unit,
    openSettings: () -> Unit,
    resetIconContent: () -> Unit
) {
    val project = LocalProject.current

    val scope = rememberCoroutineScope()
    val filePicker = rememberFilePicker()

    PluginUI(
        content = state.iconContent,
        onChooseFile = {
            scope.launch {
                val file = filePicker.launch()

                if (file != null) {
                    onSelectFile(file)
                }
            }
        },
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
    val dragAndDropHandler = rememberFileDragAndDropHandler(onDrop = onSelectFile)
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