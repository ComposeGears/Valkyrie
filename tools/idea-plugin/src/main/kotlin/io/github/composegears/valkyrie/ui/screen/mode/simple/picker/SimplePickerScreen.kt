package io.github.composegears.valkyrie.ui.screen.mode.simple.picker

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.ui.foundation.picker.GenericPickerScreen
import io.github.composegears.valkyrie.ui.platform.picker.rememberSvgXmlPathPicker
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerEvent
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.launch

val SimplePickerScreen by navDestination {
    val navController = navController()
    val coroutineScope = rememberCoroutineScope()
    val svgXmlPicker = rememberSvgXmlPathPicker()

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

    GenericPickerScreen(
        title = stringResource("simple.picker.title"),
        onBack = navController::back,
        onFilePick = { path ->
            coroutineScope.launch {
                viewModel.onAction(OnDragAndDropPath(path))
            }
        },
        onTextPaste = { text ->
            viewModel.onAction(OnPasteFromClipboard(text))
        },
        onBrowseClick = {
            coroutineScope.launch {
                svgXmlPicker.launch()?.let { path ->
                    viewModel.onAction(OnDragAndDropPath(path))
                }
            }
        },
        onOpenSettings = {
            navController.navigate(dest = SettingsScreen)
        },
    )
}
