package io.github.composegears.valkyrie.ui.common.picker

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.colors.primaryColor
import io.github.composegears.valkyrie.jewel.colors.softContentColor
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent.PickDirectory
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent.PickFiles
import io.github.composegears.valkyrie.ui.foundation.dashedBorder
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
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Link
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.typography

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
                CenterVerticalRow {
                    Icon(
                        key = AllIconsKeys.Actions.AddFile,
                        contentDescription = null,
                    )
                    Spacer(8.dp)
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = stringResource("universal.picker.dnd"),
                        maxLines = 2,
                    )
                }
            }
            InfoText(
                text = when (os) {
                    Os.MacOS -> stringResource("generic.picker.clipboard.mac")
                    else -> stringResource("generic.picker.clipboard.other")
                },
                maxLines = 2,
            )
            Spacer(16.dp)
            Text(
                text = stringResource("universal.picker.dnd.or"),
                style = JewelTheme.typography.regular,
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {
                Link(
                    text = stringResource("universal.picker.pick.directory"),
                    onClick = onPickDirectory,
                )
                Link(
                    text = stringResource("universal.picker.pick.files"),
                    onClick = onPickFiles,
                )
            }
        }
    }
}

@Composable
private fun DragAndDropBox(
    isDragging: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    val dashColor = JewelTheme.softContentColor
    val border by animateDpAsState(if (isDragging) 4.dp else 1.dp)

    Box(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .heightIn(min = 300.dp)
            .clip(shape)
            .dashedBorder(
                strokeWidth = border,
                gapWidth = 8.dp,
                dashWidth = 8.dp,
                color = dashColor,
                shape = shape,
            )
            .padding(2.dp)
            .background(
                color = when {
                    isDragging -> JewelTheme.primaryColor.copy(alpha = 0.05f)
                    else -> Color.Transparent
                },
                shape = shape,
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
