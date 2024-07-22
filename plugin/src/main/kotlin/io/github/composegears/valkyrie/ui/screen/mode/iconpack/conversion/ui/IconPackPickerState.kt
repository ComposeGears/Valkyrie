package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.dashedBorder
import io.github.composegears.valkyrie.ui.foundation.dnd.rememberMultiSelectDragAndDropHandler
import io.github.composegears.valkyrie.ui.foundation.icons.Collections
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.picker.rememberDirectoryPicker
import io.github.composegears.valkyrie.ui.foundation.picker.rememberMultipleFilesPicker
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.PickerEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.PickerEvent.PickDirectory
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.PickerEvent.PickFiles
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

@Composable
fun IconPackPickerState(onPickerEvent: (PickerEvent) -> Unit) {
    val scope = rememberCoroutineScope()

    val multipleFilePicker = rememberMultipleFilesPicker()
    val directoryPicker = rememberDirectoryPicker()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SelectableState(
            onSelectPath = { paths ->
                when {
                    paths.size == 1 -> {
                        val path = paths.first()

                        when {
                            path.isDirectory() -> onPickerEvent(PickDirectory(path = path))
                            path.isRegularFile() -> onPickerEvent(PickFiles(paths = paths))
                        }
                    }
                    else -> onPickerEvent(PickFiles(paths = paths))
                }
            },
            onPickDirectory = {
                scope.launch {
                    val path = directoryPicker.launch()

                    if (path != null) {
                        onPickerEvent(PickDirectory(path = path))
                    }
                }
            },
            onPickFiles = {
                scope.launch {
                    val paths = multipleFilePicker.launch()

                    if (paths.isNotEmpty()) {
                        onPickerEvent(PickFiles(paths = paths))
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectableState(
    onPickDirectory: () -> Unit,
    onPickFiles: () -> Unit,
    onSelectPath: (List<Path>) -> Unit
) {
    val dragAndDropHandler = rememberMultiSelectDragAndDropHandler(onDrop = onSelectPath)
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) { dragAndDropHandler.isDragging }

    DragAndDropBox(isDragging = isDragging) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = ValkyrieIcons.Collections,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Drag & drop\n\nor",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                TextButton(onClick = onPickDirectory) {
                    Text(text = "Pick dir")
                }
                TextButton(onClick = onPickFiles) {
                    Text(text = "Pick files")
                }
            }
        }
    }
}

@Composable
private fun DragAndDropBox(
    isDragging: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val dashColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val border by animateDpAsState(if (isDragging) 4.dp else 1.dp)

    Box(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.small)
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
                    isDragging -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    else -> Color.Transparent
                },
                shape = MaterialTheme.shapes.small
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Preview
@Composable
private fun PreviewPickerState() = PreviewTheme {
    IconPackPickerState(onPickerEvent = {})
}
