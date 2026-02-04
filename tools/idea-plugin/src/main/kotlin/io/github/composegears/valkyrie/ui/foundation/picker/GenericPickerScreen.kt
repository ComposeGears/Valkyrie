package io.github.composegears.valkyrie.ui.foundation.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.platform.ClipboardDataType
import io.github.composegears.valkyrie.jewel.platform.Os
import io.github.composegears.valkyrie.jewel.platform.rememberCurrentOs
import io.github.composegears.valkyrie.jewel.platform.rememberMultiSelectDragAndDropHandler
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.ui.foundation.ClipboardEventColumn
import io.github.composegears.valkyrie.jewel.ui.DragAndDropBox
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.util.stringResource
import java.nio.file.Path
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Link
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

/**
 * Generic picker screen component that can be reused for different conversion modes.
 *
 * @param title The title to display in the app bar
 * @param description Optional description text to show below the drag & drop area
 * @param fileFilter Optional predicate to filter files during drag and drop
 * @param onBack Callback when back button is clicked
 * @param onFilePick Callback when a file is selected via drag & drop or file picker
 * @param onTextPaste Callback when text is pasted from clipboard
 * @param onBrowseClick Callback when the user clicks to open the file picker
 */
@Composable
fun GenericPickerScreen(
    title: String,
    onBack: () -> Unit,
    onFilePick: (Path) -> Unit,
    onTextPaste: (String) -> Unit,
    onBrowseClick: () -> Unit,
    onOpenSettings: () -> Unit,
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
                    file?.let(onFilePick)
                }
                is ClipboardDataType.Text -> onTextPaste(dataType.text)
            }
        },
    ) {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = title)
            WeightSpacer()
            SettingsAction(openSettings = onOpenSettings)
        }
        WeightSpacer(weight = 0.3f)
        PickerBox(
            description = description,
            onDragAndDrop = onFilePick,
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
            CenterVerticalRow {
                Icon(
                    key = AllIconsKeys.Actions.AddFile,
                    contentDescription = null,
                )
                Spacer(8.dp)
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource("generic.picker.title"),
                    maxLines = 2,
                )
                Spacer(4.dp)
                Link(
                    text = stringResource("generic.picker.action"),
                    onClick = onBrowseClick,
                )
            }
            if (description != null) {
                InfoText(text = description)
                Spacer(4.dp)
            }
            InfoText(
                text = when (os) {
                    Os.MacOS -> stringResource("generic.picker.clipboard.mac")
                    else -> stringResource("generic.picker.clipboard.other")
                },
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
        onFilePick = {},
        onTextPaste = {},
        onBrowseClick = {},
        onOpenSettings = {},
    )
}
