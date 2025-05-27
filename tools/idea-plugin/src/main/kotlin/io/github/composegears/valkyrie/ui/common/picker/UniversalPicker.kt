package io.github.composegears.valkyrie.ui.common.picker

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.ui.util.disabled
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent.PickDirectory
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent.PickFiles
import io.github.composegears.valkyrie.ui.foundation.dashedBorder
import io.github.composegears.valkyrie.ui.foundation.icons.AddFile
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.ClipboardDataType
import io.github.composegears.valkyrie.ui.platform.Os
import io.github.composegears.valkyrie.ui.platform.picker.rememberDirectoryPicker
import io.github.composegears.valkyrie.ui.platform.picker.rememberMultipleFilesPicker
import io.github.composegears.valkyrie.ui.platform.rememberCurrentOs
import io.github.composegears.valkyrie.ui.platform.rememberMultiSelectDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.ClipboardEventColumn
import io.github.composegears.valkyrie.util.stringResource
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlinx.coroutines.launch

@Composable
fun UniversalPicker(
    onPickerEvent: (PickerEvent) -> Unit,
    modifier: Modifier = Modifier,
    headerSection: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val multipleFilePicker = rememberMultipleFilesPicker()
    val directoryPicker = rememberDirectoryPicker()

    ClipboardEventColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        onPaste = { dataType ->
            when (dataType) {
                is ClipboardDataType.Files -> onPickerEvent(PickFiles(paths = dataType.paths))
                is ClipboardDataType.Text -> onPickerEvent(PickerEvent.ClipboardText(dataType.text))
            }
        },
    ) {
        headerSection()
        WeightSpacer(weight = 0.3f)
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
            },
        )
        WeightSpacer(weight = 0.7f)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectableState(
    onPickDirectory: () -> Unit,
    onPickFiles: () -> Unit,
    onSelectPath: (List<Path>) -> Unit,
) {
    val dragAndDropHandler = rememberMultiSelectDragAndDropHandler(onDrop = onSelectPath)
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) { dragAndDropHandler.isDragging }

    val os = rememberCurrentOs()

    DragAndDropBox(isDragging = isDragging) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CenterVerticalRow {
                Icon(
                    imageVector = ValkyrieIcons.AddFile,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource("picker.dnd"),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Text(
                text = when (os) {
                    Os.MacOS -> stringResource("picker.clipboard.mac")
                    else -> stringResource("picker.clipboard.other")
                },
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.disabled(),
                style = MaterialTheme.typography.labelSmall,
            )
            VerticalSpacer(16.dp)
            Text(
                text = stringResource("picker.dnd.or"),
                style = MaterialTheme.typography.labelMedium,
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                TextButton(onClick = onPickDirectory) {
                    Text(text = stringResource("picker.pick.directory"))
                }
                TextButton(onClick = onPickFiles) {
                    Text(text = stringResource("picker.pick.files"))
                }
            }
        }
    }
}

@Composable
private fun DragAndDropBox(
    isDragging: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
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
                shape = MaterialTheme.shapes.small,
            )
            .padding(2.dp)
            .background(
                color = when {
                    isDragging -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    else -> Color.Transparent
                },
                shape = MaterialTheme.shapes.small,
            ),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

@Preview
@Composable
private fun UniversalPickerPreview() = PreviewTheme {
    UniversalPicker(
        onPickerEvent = {},
        headerSection = {},
    )
}
