package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.dnd.rememberDragAndDropFolderHandler
import io.github.composegears.valkyrie.ui.foundation.icons.Folder
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.picker.rememberDirectoryPicker
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.SaveDestination
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.SelectDestinationFolder
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState.ChooseExportDirectoryState
import kotlinx.coroutines.launch

@Composable
fun ChoosePackDirectory(
    state: ChooseExportDirectoryState,
    modifier: Modifier = Modifier,
    onAction: (NewPackAction) -> Unit,
) {
    val dragAndDropHandler = rememberDragAndDropFolderHandler(
        onDrop = { path ->
            onAction(SelectDestinationFolder(path))
        },
    )
    val isDragging by remember(dragAndDropHandler.isDragging) { mutableStateOf(dragAndDropHandler.isDragging) }

    val scope = rememberCoroutineScope()
    val directoryPicker = rememberDirectoryPicker()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DragAndDropBox(
            modifier = Modifier.fillMaxWidth(),
            isDragging = isDragging,
            onChoose = {
                scope.launch {
                    val path = directoryPicker.launch()

                    if (path != null) {
                        onAction(SelectDestinationFolder(path))
                    }
                }
            },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = ValkyrieIcons.Folder,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    text = "Drag & Drop folder\nor browse",
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
        VerticalSpacer(36.dp)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            if (state.iconPackDestination.isNotEmpty()) {
                Column {
                    Text(
                        text = "Export path:",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = state.iconPackDestination,
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
            if (state.predictedPackage.isNotEmpty()) {
                Column {
                    Text(
                        text = "Predicted package:",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        textDecoration = TextDecoration.Underline,
                        text = state.predictedPackage,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
        Button(
            modifier = Modifier.align(Alignment.End),
            enabled = state.nextAvailable,
            onClick = { onAction(SaveDestination) },
        ) {
            Text(text = "Next")
        }
    }
}

@Preview
@Composable
private fun ChoosePackDirectoryPreview() = PreviewTheme {
    ChoosePackDirectory(
        modifier = Modifier.fillMaxWidth(0.8f),
        state = ChooseExportDirectoryState(
            iconPackDestination = "path/to/export",
            predictedPackage = "com.example.iconpack",
            nextAvailable = true,
        ),
        onAction = {},
    )
}
