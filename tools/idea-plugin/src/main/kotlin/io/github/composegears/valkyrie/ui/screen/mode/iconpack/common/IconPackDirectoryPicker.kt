package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

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
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.platform.picker.rememberDirectoryPicker
import io.github.composegears.valkyrie.jewel.platform.rememberDragAndDropFolderHandler
import io.github.composegears.valkyrie.jewel.platform.rememberProjectAccessor
import io.github.composegears.valkyrie.jewel.settings.InfoSettingsRow
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.jewel.ui.DragAndDropBox
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.DirectoryState
import io.github.composegears.valkyrie.util.stringResource
import java.nio.file.Path
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun IconPackDirectoryPicker(
    state: DirectoryState,
    onSelectPath: (Path) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dragAndDropHandler = rememberDragAndDropFolderHandler(onDrop = onSelectPath)
    val isDragging by remember(dragAndDropHandler.isDragging) { mutableStateOf(dragAndDropHandler.isDragging) }

    val scope = rememberCoroutineScope()
    val directoryPicker = rememberDirectoryPicker()

    Column(
        modifier = modifier
            .widthIn(max = 450.dp)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DragAndDropBox(
            modifier = Modifier.fillMaxWidth(),
            isDragging = isDragging,
            onChoose = {
                scope.launch {
                    val path = directoryPicker.launch()

                    if (path != null) {
                        onSelectPath(path)
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
                val projectAccessor = rememberProjectAccessor()

                InfoSettingsRow(
                    text = stringResource("iconpack.newpack.choose.directory.destination.path"),
                    infoText = "~${state.iconPackDestination.replace(projectAccessor.path.orEmpty(), "")}",
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
            onClick = onNext,
        ) {
            Text(text = stringResource("iconpack.newpack.choose.directory.continue"))
        }
    }
}

@Preview
@Composable
private fun IconPackDirectoryPickerPreview() = PreviewTheme(alignment = Alignment.Center) {
    val projectAccessor = rememberProjectAccessor()

    IconPackDirectoryPicker(
        state = DirectoryState(
            iconPackDestination = "${projectAccessor.path}/tools/idea-plugin/src/main/kotlin/io/github/composegears/valkyrie/icons",
            predictedPackage = "io.github.composegears.valkyrie.icons",
            nextAvailable = true,
        ),
        onSelectPath = {},
        onNext = {},
    )
}
