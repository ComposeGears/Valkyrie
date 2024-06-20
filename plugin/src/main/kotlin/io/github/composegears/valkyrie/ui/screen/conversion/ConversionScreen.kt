package io.github.composegears.valkyrie.ui.screen.conversion

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.NavDestination
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navDestination
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.intellij.openapi.ide.CopyPasteManager
import io.github.composegears.valkyrie.ui.components.IntellijEditorTextField
import io.github.composegears.valkyrie.ui.components.dashedBorder
import io.github.composegears.valkyrie.ui.components.rememberDragAndDropHandler
import io.github.composegears.valkyrie.ui.icons.Collections
import io.github.composegears.valkyrie.ui.icons.ContentCopy
import io.github.composegears.valkyrie.ui.icons.ValkyrieIcons
import java.awt.datatransfer.StringSelection
import java.io.File

val ConversionScreen: NavDestination<Unit> by navDestination {
    val conversionViewModel = koinTiamatViewModel<ConversionViewModel>()
    val state by conversionViewModel.state.collectAsState()

    val dragAndDropHandler = rememberDragAndDropHandler {
        conversionViewModel.parseIcon(it)
    }

    val isDragging by remember(dragAndDropHandler.isDragging) { mutableStateOf(dragAndDropHandler.isDragging) }

    Column(modifier = Modifier.fillMaxSize()) {
        ConversionUi(
            state = state,
            isDragging = isDragging,
            onSelectFile = {
                conversionViewModel.parseIcon(it)
                conversionViewModel.updateLastChoosePath(it)
            },
            openSettings = { },
            resetIconContent = conversionViewModel::resetIconContent
        )
    }
}

@Composable
private fun ConversionUi(
    state: ConversionState,
    isDragging: Boolean,
    onSelectFile: (File) -> Unit,
    openSettings: () -> Unit,
    resetIconContent: () -> Unit
) {
    var showFilePicker by remember { mutableStateOf(false) }

    PluginUI(
        content = state.iconContent,
        isDragging = isDragging,
        onChooseFile = { showFilePicker = true },
        onClear = resetIconContent,
        onCopy = {
            val text = state.iconContent ?: return@PluginUI
            CopyPasteManager.getInstance().setContents(StringSelection(text))
        },
        openSettings = openSettings
    )

    FilePicker(
        show = showFilePicker,
        fileExtensions = listOf("svg", "xml"),
        initialDirectory = state.initialDirectory,
        onFileSelected = { mpFile ->
            if (mpFile != null) {
                onSelectFile(File(mpFile.path))
                showFilePicker = false
            } else {
                showFilePicker = false
            }
        }
    )
}

@Composable
private fun PluginUI(
    content: String?,
    isDragging: Boolean,
    onChooseFile: () -> Unit,
    onClear: () -> Unit,
    onCopy: () -> Unit,
    openSettings: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (content != null) {
                    IconButton(onClick = onClear) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = openSettings) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                }
            }

            if (content != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    IntellijEditorTextField(
                        modifier = Modifier.fillMaxSize(),
                        text = content
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = onCopy
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = ValkyrieIcons.ContentCopy,
                            contentDescription = null
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SelectableState(
                        isDragging = isDragging,
                        onChooseFile = onChooseFile
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SelectableState(
    isDragging: Boolean,
    onChooseFile: () -> Unit
) {
    var isHover by remember(isDragging) { mutableStateOf(isDragging) }

    val dashColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val border by animateDpAsState(if (isHover) 4.dp else 1.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.small)
            .onPointerEvent(PointerEventType.Enter) { isHover = true }
            .onPointerEvent(PointerEventType.Exit) { isHover = false }
            .dashedBorder(
                strokeWidth = border,
                gapWidth = 8.dp,
                dashWidth = 8.dp,
                color = dashColor,
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onChooseFile),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = ValkyrieIcons.Collections,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Drag & Drop or browse",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Supports: SVG, XML",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}