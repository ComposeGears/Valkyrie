package io.github.composegears.valkyrie.ui.screen.conversion

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.NavDestination
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navDestination
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.intellij.openapi.ide.CopyPasteManager
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.ui.components.IntellijEditorTextField
import java.awt.datatransfer.StringSelection
import java.io.File

val ConversionScreen: NavDestination<Unit> by navDestination {
    val conversionViewModel = koinTiamatViewModel<ConversionViewModel>()
    val state by conversionViewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        ConversionUi(
            state = state,
            onPathChange = conversionViewModel::updateLastChoosePath
        )
    }
}

@Composable
private fun ConversionUi(
    state: ConversionState,
    onPathChange: (String) -> Unit
) {
    var showFilePicker by remember { mutableStateOf(false) }
    var file by remember { mutableStateOf<File?>(null) }

    var content by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(file) {
        val iconFile = file ?: return@LaunchedEffect

        content = IconParser.tryParse(iconFile, state.config!!)
    }

    PluginUI(
        content = content,
        onChooseFile = { showFilePicker = true },
        onClear = { content = null },
        onCopy = {
            val text = content ?: return@PluginUI
            CopyPasteManager.getInstance().setContents(StringSelection(text))
        }
    )

    FilePicker(
        show = showFilePicker,
        fileExtensions = listOf("svg", "xml"),
        initialDirectory = state.initialDirectory,
        onFileSelected = { mpFile ->
            if (mpFile != null) {
                file = File(mpFile.path)
                file?.parentFile?.path?.run(onPathChange)
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
    onChooseFile: () -> Unit,
    onClear: () -> Unit,
    onCopy: () -> Unit
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
                Button(onClick = onChooseFile) {
                    Text(text = "Choose")
                }
                if (content != null) {
                    IconButton(onClick = onClear) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { }) {
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
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        modifier = Modifier.padding(32.dp),
                        text = "Choose SVG or XML to convert to Compose ImageVector format",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}