package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.composegears.tiamat.rememberSaveableViewModel
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.rememberSnackbar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.copyInClipboard
import io.github.composegears.valkyrie.ui.platform.picker.rememberFilePicker
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.SimpleConversionPickerStateUI
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.preview.SimpleConversionPreviewStateUi
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.Back
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.ClosePreview
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnCopyInClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnIconNaneChange
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OpenFilePicker
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionAction.OpenSettings
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.ConversionState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionState.PickerState
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel.SimpleConversionViewModel
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

val SimpleConversionScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = rememberSaveableViewModel(::SimpleConversionViewModel)
    val state by viewModel.state.collectAsState()
    val settings by viewModel.inMemorySettings.settings.collectAsState()

    val scope = rememberCoroutineScope()
    val filePicker = rememberFilePicker()
    val snackbar = rememberSnackbar()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach(snackbar::show)
            .launchIn(this)
    }

    ConversionUi(
        state = state,
        previewType = settings.previewType,
        onAction = {
            when (it) {
                is Back -> navController.back(transition = navigationSlideInOut(false))
                is OpenSettings -> navController.navigate(
                    dest = SettingsScreen,
                    transition = navigationSlideInOut(true),
                )
                is ClosePreview -> viewModel.reset()
                is OpenFilePicker -> {
                    scope.launch {
                        val path = filePicker.launch()

                        if (path != null) {
                            viewModel.selectPath(path)
                        }
                    }
                }
                is OnPasteFromClipboard -> viewModel.pasteFromClipboard(it.text)
                is OnDragAndDropPath -> viewModel.selectPath(it.path)
                is OnCopyInClipboard -> {
                    copyInClipboard(it.text)
                    snackbar.show(message = "Copied in clipboard")
                }
                is OnIconNaneChange -> viewModel.changeIconName(it.name)
            }
        },
    )
}

@Composable
private fun ConversionUi(
    state: SimpleConversionState,
    previewType: PreviewType,
    onAction: (SimpleConversionAction) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = state,
            transitionSpec = {
                fadeIn(tween(220, delayMillis = 90)) togetherWith fadeOut(tween(90))
            },
            contentKey = {
                when (it) {
                    is ConversionState -> 0
                    is PickerState -> 1
                }
            },
        ) { current ->
            when (current) {
                is PickerState -> SimpleConversionPickerStateUI(onAction = onAction)
                is ConversionState -> SimpleConversionPreviewStateUi(
                    state = current,
                    previewType = previewType,
                    onAction = onAction,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SimpleConversionScreenPreview() = PreviewTheme {
    ConversionUi(
        state = PickerState,
        previewType = PreviewType.Auto,
        onAction = {},
    )
}
