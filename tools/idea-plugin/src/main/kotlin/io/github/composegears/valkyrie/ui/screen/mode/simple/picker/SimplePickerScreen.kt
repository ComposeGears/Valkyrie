package io.github.composegears.valkyrie.ui.screen.mode.simple.picker

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
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
import io.github.composegears.valkyrie.ui.platform.picker.rememberFilePicker
import io.github.composegears.valkyrie.ui.platform.rememberCurrentOs
import io.github.composegears.valkyrie.ui.platform.rememberFileDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.ClipboardEventColumn
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerEvent
import java.nio.file.Path
import kotlinx.coroutines.launch

val SimplePickerScreen by navDestination {
    val navController = navController()
    val coroutineScope = rememberCoroutineScope()
    val filePicker = rememberFilePicker()

    val viewModel = viewModel { SimplePickerViewModel() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SimplePickerEvent.NavigateToConversion -> {
                    navController.navigate(
                        dest = SimpleConversionScreen,
                        navArgs = event.params,
                    )
                }
            }
        }
    }

    SimplePickerScreenContent(
        onBack = navController::back,
        openFilePicker = {
            coroutineScope.launch {
                filePicker.launch()?.let {
                    viewModel.onAction(OnDragAndDropPath(it))
                }
            }
        },
        onAction = viewModel::onAction,
    )
}

@Composable
private fun SimplePickerScreenContent(
    onBack: () -> Unit,
    openFilePicker: () -> Unit,
    onAction: (SimplePickerAction) -> Unit,
) {
    ClipboardEventColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        onPaste = { dataType ->
            when (dataType) {
                is ClipboardDataType.Files -> onAction(OnDragAndDropPath(dataType.paths.first()))
                is ClipboardDataType.Text -> onAction(OnPasteFromClipboard(dataType.text))
            }
        },
    ) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = "Simple conversion")
        }
        WeightSpacer(weight = 0.3f)
        PickerBox(
            onDragAndDrop = { onAction(OnDragAndDropPath(it)) },
            onOpenFilePicker = openFilePicker,
        )
        WeightSpacer(weight = 0.7f)
    }
}

@Composable
private fun PickerBox(
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
                        append("Drag & drop or ")
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
            Text(
                text = when (os) {
                    Os.MacOS -> "Cmd+V to paste from clipboard"
                    else -> "Ctrl+V to paste from clipboard"
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
private fun SimplePickerScreenContentPreview() = PreviewTheme {
    SimplePickerScreenContent(
        onBack = {},
        openFilePicker = {},
        onAction = {},
    )
}
