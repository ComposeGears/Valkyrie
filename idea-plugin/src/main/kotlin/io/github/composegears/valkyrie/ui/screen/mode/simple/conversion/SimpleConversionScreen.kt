package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinSaveableTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.ClearAction
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.DragAndDropBox
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.disabled
import io.github.composegears.valkyrie.ui.foundation.highlights.KotlinCodeViewer
import io.github.composegears.valkyrie.ui.foundation.icons.AddFile
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.onPasteEvent
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.rememberSnackbar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.Os
import io.github.composegears.valkyrie.ui.platform.copyInClipboard
import io.github.composegears.valkyrie.ui.platform.pasteFromClipboard
import io.github.composegears.valkyrie.ui.platform.picker.rememberFilePicker
import io.github.composegears.valkyrie.ui.platform.rememberCurrentOs
import io.github.composegears.valkyrie.ui.platform.rememberFileDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import java.nio.file.Path
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

val SimpleConversionScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = koinSaveableTiamatViewModel<SimpleConversionViewModel>()
    val state by viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val filePicker = rememberFilePicker()
    val snackbar = rememberSnackbar()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach(snackbar::show)
            .launchIn(this)
    }

    ConversionUi(
        content = state.iconContent,
        onBack = {
            navController.back(transition = navigationSlideInOut(false))
        },
        onChoosePath = {
            scope.launch {
                val path = filePicker.launch()

                if (path != null) {
                    viewModel.selectPath(path)
                }
            }
        },
        onClear = viewModel::reset,
        onCopy = {
            copyInClipboard(it)
            snackbar.show(message = "Copied in clipboard")
        },
        onSelectPath = viewModel::selectPath,
        openSettings = {
            navController.navigate(
                dest = SettingsScreen,
                transition = navigationSlideInOut(true),
            )
        },
        onPaste = viewModel::pasteFromClipboard,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ConversionUi(
    content: String?,
    onBack: () -> Unit,
    onChoosePath: () -> Unit,
    onClear: () -> Unit,
    onCopy: (String) -> Unit,
    onSelectPath: (Path) -> Unit,
    onPaste: (String) -> Unit,
    openSettings: () -> Unit,
) {
    var codePreview by rememberMutableState(content) { content.orEmpty() }
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusProperties { exit = { focusRequester } }
            .focusable()
            .onPointerEvent(PointerEventType.Enter) {
                focusRequester.requestFocus()
            }
            .onPointerEvent(PointerEventType.Exit) {
                focusRequester.freeFocus()
            }
            .onPasteEvent {
                pasteFromClipboard()?.let(onPaste)
            },
    ) {
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

    LaunchedEffect(content) {
        if (content == null) {
            focusRequester.requestFocus()
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

    val os = rememberCurrentOs()

    DragAndDropBox(
        isDragging = isDragging,
        onChoose = onChoosePath,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
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
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Text(
                text = when (os) {
                    Os.MacOS -> "Cmd+V to paste from clipboard"
                    else -> "Ctrl+V to paste from clipboard"
                },
                color = LocalContentColor.current.disabled(),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview
@Composable
private fun SimpleConversionScreenPreview() = PreviewTheme {
    ConversionUi(
        content = null,
        onBack = {},
        onChoosePath = {},
        onClear = {},
        onCopy = {},
        onSelectPath = {},
        onPaste = {},
        openSettings = {},
    )
}
