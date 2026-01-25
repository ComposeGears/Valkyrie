package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.platform.rememberCurrentProject
import io.github.composegears.valkyrie.jewel.settings.InfoSettingsRow
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.platform.picker.rememberDirectoryPicker
import io.github.composegears.valkyrie.ui.platform.rememberDragAndDropFolderHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.SaveDestination
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.SelectDestinationFolder
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState.ChooseDestinationDirectoryState
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.launch
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun ChoosePackDirectory(
    state: ChooseDestinationDirectoryState,
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
        modifier = modifier
            .widthIn(max = 450.dp)
            .padding(horizontal = 16.dp),
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
                    key = AllIconsKeys.Nodes.Folder,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource("iconpack.newpack.choose.directory.dnd"),
                )
            }
        }
        Spacer(24.dp)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (state.iconPackDestination.isNotEmpty()) {
                val currentProject = rememberCurrentProject()

                InfoSettingsRow(
                    text = stringResource("iconpack.newpack.choose.directory.destination.path"),
                    infoText = "~${state.iconPackDestination.replace(currentProject.path.orEmpty(), "")}",
                )
            }
            if (state.predictedPackage.isNotEmpty()) {
                InfoSettingsRow(
                    text = stringResource("iconpack.newpack.choose.directory.predicted.package"),
                    infoText = state.predictedPackage,
                )
            }
        }
        Spacer(16.dp)
        DefaultButton(
            modifier = Modifier.align(Alignment.End),
            enabled = state.nextAvailable,
            onClick = { onAction(SaveDestination) },
        ) {
            Text(text = stringResource("iconpack.newpack.choose.directory.continue"))
        }
    }
}

@Preview
@Composable
private fun ChoosePackDirectoryPreview() = PreviewTheme(alignment = Alignment.Center) {
    val currentProject = rememberCurrentProject()

    ChoosePackDirectory(
        state = ChooseDestinationDirectoryState(
            iconPackDestination = "${currentProject.path}/tools/idea-plugin/src/main/kotlin/io/github/composegears/valkyrie/icons",
            predictedPackage = "io.github.composegears.valkyrie.icons",
            nextAvailable = true,
        ),
        onAction = {},
    )
}
