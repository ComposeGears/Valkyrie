package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import io.github.composegears.valkyrie.ui.foundation.notification.rememberNotificationManager
import io.github.composegears.valkyrie.ui.foundation.picker.rememberFilePicker
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.launch
import java.awt.datatransfer.StringSelection
import java.nio.file.Path

val SimpleConversionScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = koinTiamatViewModel<SimpleConversionViewModel>()
    val state by viewModel.state.collectAsState()

    ConversionUi(
        state = state,
        onSelectPath = viewModel::selectPath,
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
    onSelectPath: (Path) -> Unit,
    openSettings: () -> Unit,
    resetIconContent: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val filePicker = rememberFilePicker()
    val notificationManager = rememberNotificationManager()

    PluginUI(
        content = state.iconContent,
        onChoosePath = {
            scope.launch {
                val path = filePicker.launch()

                if (path != null) {
                    onSelectPath(path)
                }
            }
        },
        onClear = resetIconContent,
        onCopy = {
            val text = state.iconContent ?: return@PluginUI
            CopyPasteManager.getInstance().setContents(StringSelection(text))
            notificationManager.show("Copied in clipboard")
        },
        onSelectPath = onSelectPath,
        openSettings = openSettings
    )
}

@Composable
private fun PluginUI(
    content: String?,
    onChoosePath: () -> Unit,
    onClear: () -> Unit,
    onCopy: () -> Unit,
    onSelectPath: (Path) -> Unit,
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
                    onSelectPath = onSelectPath,
                    onChoosePath = onChoosePath
                )
            }
        }
    }
}

@Composable
private fun SelectableState(
    onChoosePath: () -> Unit,
    onSelectPath: (Path) -> Unit
) {
    val dragAndDropHandler = rememberFileDragAndDropHandler(onDrop = onSelectPath)
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) { dragAndDropHandler.isDragging }

    DragAndDropBox(
        isDragging = isDragging,
        onChoose = onChoosePath
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

@Preview
@Composable
private fun SimpleConversionScreenPreview() = PreviewTheme {
    ConversionUi(
        state = SimpleConversionState(),
        onSelectPath = {},
        openSettings = {},
        resetIconContent = {}
    )
}