package io.github.composegears.valkyrie.ui.foundation.picker

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.idea.AddFile
import io.github.composegears.valkyrie.compose.icons.idea.AddFileDark
import io.github.composegears.valkyrie.compose.util.disabled
import io.github.composegears.valkyrie.compose.util.isLight
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.ClipboardDataType
import io.github.composegears.valkyrie.ui.platform.Os
import io.github.composegears.valkyrie.ui.platform.rememberCurrentOs
import io.github.composegears.valkyrie.ui.platform.rememberMultiSelectDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.ClipboardEventColumn
import io.github.composegears.valkyrie.util.stringResource
import java.nio.file.Path

/**
 * Generic picker screen component that can be reused for different conversion modes.
 *
 * @param title The title to display in the app bar
 * @param description Optional description text to show below the drag & drop area
 * @param fileFilter Optional predicate to filter files during drag and drop
 * @param onBack Callback when back button is clicked
 * @param onFilePicked Callback when a file is selected via drag & drop or file picker
 * @param onTextPasted Callback when text is pasted from clipboard
 * @param onBrowseClick Callback when the user clicks to open the file picker
 */
@Composable
fun GenericPickerScreen(
    title: String,
    onBack: () -> Unit,
    onFilePicked: (Path) -> Unit,
    onTextPasted: (String) -> Unit,
    onBrowseClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    fileFilter: (Path) -> Boolean = { true },
) {
    ClipboardEventColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        onPaste = { dataType ->
            when (dataType) {
                is ClipboardDataType.Files -> {
                    val file = dataType.paths.firstOrNull { fileFilter(it) }
                    file?.let(onFilePicked)
                }
                is ClipboardDataType.Text -> onTextPasted(dataType.text)
            }
        },
    ) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = title)
        }
        WeightSpacer(weight = 0.3f)
        PickerBox(
            description = description,
            onDragAndDrop = onFilePicked,
            onBrowseClick = onBrowseClick,
            fileFilter = fileFilter,
        )
        WeightSpacer(weight = 0.7f)
    }
}

@Composable
private fun PickerBox(
    onDragAndDrop: (Path) -> Unit,
    onBrowseClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    fileFilter: (Path) -> Boolean = { true },
) {
    val dragAndDropHandler = rememberMultiSelectDragAndDropHandler { paths ->
        val filteredPath = paths.firstOrNull(fileFilter)
        filteredPath?.let(onDragAndDrop)
    }
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) {
        dragAndDropHandler.isDragging
    }

    val os = rememberCurrentOs()

    DragAndDropBox(
        modifier = modifier,
        isDragging = isDragging,
        onChoose = onBrowseClick,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                imageVector = when {
                    MaterialTheme.colorScheme.isLight -> ValkyrieIcons.Idea.AddFile
                    else -> ValkyrieIcons.Idea.AddFileDark
                },
                contentDescription = null,
            )
            VerticalSpacer(8.dp)
            CenterVerticalRow {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = buildAnnotatedString {
                        append(stringResource("picker.dnd"))
                        append(" ")
                        append(stringResource("picker.dnd.or"))
                        append(" ")
                        append(
                            AnnotatedString(
                                text = "Browse",
                                spanStyle = SpanStyle(MaterialTheme.colorScheme.primary),
                            ),
                        )
                    },
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            if (description != null) {
                Text(
                    text = description,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    color = LocalContentColor.current.disabled(),
                    style = MaterialTheme.typography.labelSmall,
                )
                VerticalSpacer(4.dp)
            }

            Text(
                text = when (os) {
                    Os.MacOS -> stringResource("picker.clipboard.mac")
                    else -> stringResource("picker.clipboard.other")
                },
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = LocalContentColor.current.disabled(),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview
@Composable
private fun GenericPickerScreenPreview() = PreviewTheme {
    GenericPickerScreen(
        title = "Simple conversion",
        description = "Drop SVG/XML file",
        onBack = {},
        onFilePicked = {},
        onTextPasted = {},
        onBrowseClick = {},
    )
}
