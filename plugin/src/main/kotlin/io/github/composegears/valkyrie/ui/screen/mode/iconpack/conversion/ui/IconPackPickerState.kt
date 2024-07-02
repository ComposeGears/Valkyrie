package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import com.darkrockstudios.libraries.mpfilepicker.MultipleFilePicker
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.PreviewWrapper
import io.github.composegears.valkyrie.ui.foundation.dnd.rememberMultiSelectDragAndDropHandler
import io.github.composegears.valkyrie.ui.foundation.icons.Collections
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.PickerEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.PickerEvent.PickDirectory
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.PickerEvent.PickFiles
import java.io.File

@Composable
fun IconPackPickerState(
    initialDirectory: String,
    onPickerEvent: (PickerEvent) -> Unit,
) {
    var showDirectoryPicker by rememberMutableState { false }
    var showFilesPicker by rememberMutableState { false }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SelectableState(
            onSelectFile = { files ->
                when {
                    files.size == 1 -> {
                        val file = files.first()

                        when {
                            file.isDirectory -> onPickerEvent(PickDirectory(path = file.path))
                            file.isFile -> onPickerEvent(PickFiles(files = files))
                        }
                    }
                    else -> onPickerEvent(PickFiles(files = files))
                }
            },
            onPickDirectory = { showDirectoryPicker = true },
            onPickFiles = { showFilesPicker = true },
        )
    }

    MultipleFilePicker(
        show = showFilesPicker,
        fileExtensions = listOf("svg", "xml"),
        initialDirectory = initialDirectory,
        onFileSelected = { files ->
            showFilesPicker = false

            if (!files.isNullOrEmpty()) {
                onPickerEvent(PickFiles(files = files.map { it.platformFile as File }))
            }
        }
    )
    DirectoryPicker(show = showDirectoryPicker) {
        showDirectoryPicker = false

        if (it != null) {
            onPickerEvent(PickDirectory(path = it))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectableState(
    onPickDirectory: () -> Unit,
    onPickFiles: () -> Unit,
    onSelectFile: (List<File>) -> Unit
) {
    val dragAndDropHandler = rememberMultiSelectDragAndDropHandler(onDrop = onSelectFile)
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

@Preview
@Composable
private fun PreviewPickerState() = PreviewWrapper {
    IconPackPickerState(
        initialDirectory = "",
        onPickerEvent = {}
    )
}
