package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinSaveableTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.intellij.openapi.ide.CopyPasteManager
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.ClearAction
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.highlights.KotlinCodeViewer
import io.github.composegears.valkyrie.ui.foundation.icons.Collections
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.rememberSnackbar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.picker.rememberFilePicker
import io.github.composegears.valkyrie.ui.platform.rememberFileDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import java.awt.datatransfer.StringSelection
import java.nio.file.Path
import kotlinx.coroutines.launch

val SimpleConversionScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = koinSaveableTiamatViewModel<SimpleConversionViewModel>()
    val state by viewModel.state.collectAsState()

    ConversionUi(
        state = state,
        onSelectPath = viewModel::selectPath,
        onBack = {
            navController.back(transition = navigationSlideInOut(false))
        },
        openSettings = {
            navController.navigate(
                dest = SettingsScreen,
                transition = navigationSlideInOut(true),
            )
        },
        resetIconContent = viewModel::reset,
    )
}

@Composable
private fun ConversionUi(
    state: SimpleConversionState,
    onBack: () -> Unit,
    onSelectPath: (Path) -> Unit,
    openSettings: () -> Unit,
    resetIconContent: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val filePicker = rememberFilePicker()
    val snackbar = rememberSnackbar()

    PluginUI(
        content = state.iconContent,
        onBack = onBack,
        onChoosePath = {
            scope.launch {
                val path = filePicker.launch()

                if (path != null) {
                    onSelectPath(path)
                }
            }
        },
        onClear = resetIconContent,
        onCopy = {
            CopyPasteManager.getInstance().setContents(StringSelection(it))
            snackbar.show(message = "Copied in clipboard")
        },
        onSelectPath = onSelectPath,
        openSettings = openSettings,
    )
}

@Composable
private fun PluginUI(
    content: String?,
    onBack: () -> Unit,
    onChoosePath: () -> Unit,
    onClear: () -> Unit,
    onCopy: (String) -> Unit,
    onSelectPath: (Path) -> Unit,
    openSettings: () -> Unit,
) {
    var codePreview by rememberMutableState(content) { content.orEmpty() }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = "Simple conversion")
            WeightSpacer()
            if (content != null) {
                ClearAction(onClear = onClear)
                CopyAction(onCopy = { onCopy(codePreview) })
            }
            SettingsAction(openSettings = openSettings)
        }

        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = content,
            transitionSpec = {
                fadeIn(tween(220, delayMillis = 90)) togetherWith fadeOut(tween(90))
            },
        ) { current ->
            when (current) {
                null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        WeightSpacer(weight = 0.3f)
                        SelectableState(
                            onSelectPath = onSelectPath,
                            onChoosePath = onChoosePath,
                        )
                        WeightSpacer(weight = 0.7f)
                    }
                }
                else -> {
                    KotlinCodeViewer(
                        modifier = Modifier.fillMaxSize(),
                        text = current,
                        onChange = {
                            codePreview = it
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectableState(
    onChoosePath: () -> Unit,
    onSelectPath: (Path) -> Unit,
) {
    val dragAndDropHandler = rememberFileDragAndDropHandler(onDrop = onSelectPath)
    val isDragging by rememberMutableState(dragAndDropHandler.isDragging) { dragAndDropHandler.isDragging }

    DragAndDropBox(
        isDragging = isDragging,
        onChoose = onChoosePath,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = ValkyrieIcons.Collections,
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Drag & Drop or browse",
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = "Supports: SVG, XML",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview
@Composable
private fun SimpleConversionScreenPreview() = PreviewTheme {
    ConversionUi(
        state = SimpleConversionState(),
        onSelectPath = {},
        openSettings = {},
        resetIconContent = {},
        onBack = {},
    )
}
