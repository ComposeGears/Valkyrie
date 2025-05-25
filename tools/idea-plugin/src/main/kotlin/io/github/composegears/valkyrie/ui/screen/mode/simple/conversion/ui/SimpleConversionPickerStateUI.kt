package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.ui.util.disabled
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.icons.AddFile
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.ClipboardDataType
import io.github.composegears.valkyrie.ui.platform.Os
import io.github.composegears.valkyrie.ui.platform.rememberCurrentOs
import io.github.composegears.valkyrie.ui.platform.rememberFileDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.ClipboardEventColumn
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.Back
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OpenFilePicker
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OpenSettings
import java.nio.file.Path

@Composable
fun SimpleConversionPickerStateUI(
    onAction: (SimpleConversionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ClipboardEventColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        onPaste = { dataType ->
            when (dataType) {
                is ClipboardDataType.Files -> onAction(OnDragAndDropPath(dataType.paths.first()))
                is ClipboardDataType.Text -> onAction(OnPasteFromClipboard(dataType.text))
            }
        },
    ) {
        TopAppBar {
            BackAction(onBack = { onAction(Back) })
            AppBarTitle(title = "Simple conversion")
            WeightSpacer()
            SettingsAction(openSettings = { onAction(OpenSettings) })
        }
        WeightSpacer(weight = 0.3f)
        SelectableState(
            onDragAndDrop = { onAction(OnDragAndDropPath(it)) },
            onOpenFilePicker = { onAction(OpenFilePicker) },
        )
        WeightSpacer(weight = 0.7f)
    }
}

@Composable
private fun SelectableState(
    onDragAndDrop: (Path) -> Unit,
    onOpenFilePicker: () -> Unit,
) {
    val dragAndDropHandler = rememberFileDragAndDropHandler(onDrop = onDragAndDrop)
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) { dragAndDropHandler.isDragging }

    val os = rememberCurrentOs()

    DragAndDropBox(
        isDragging = isDragging,
        onChoose = onOpenFilePicker,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CenterVerticalRow {
                Icon(
                    imageVector = ValkyrieIcons.AddFile,
                    contentDescription = null,
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = buildAnnotatedString {
                        append("Drag & drop or ")
                        append(
                            AnnotatedString(
                                text = "Browse",
                                spanStyle = SpanStyle(MaterialTheme.colorScheme.primary),
                            ),
                        )
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Text(
                text = when (os) {
                    Os.MacOS -> "Cmd+V to paste from clipboard"
                    else -> "Ctrl+V to paste from clipboard"
                },
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.disabled(),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview
@Composable
private fun SimpleConversionPickerStateUIPreview() = PreviewTheme {
    SimpleConversionPickerStateUI(onAction = {})
}
