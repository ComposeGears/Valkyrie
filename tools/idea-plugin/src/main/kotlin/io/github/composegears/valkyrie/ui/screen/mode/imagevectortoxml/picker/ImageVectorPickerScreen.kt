package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.ui.foundation.picker.GenericPickerScreen
import io.github.composegears.valkyrie.ui.platform.picker.rememberKtFilePicker
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.ImageVectorToXmlScreen
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model.ImageVectorPickerAction
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model.ImageVectorPickerEvent
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.launch

val ImageVectorPickerScreen by navDestination {
    val navController = navController()
    val coroutineScope = rememberCoroutineScope()
    val filePicker = rememberKtFilePicker()

    val viewModel = viewModel { ImageVectorPickerViewModel() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ImageVectorPickerEvent.NavigateToConversion -> {
                    navController.navigate(
                        dest = ImageVectorToXmlScreen,
                        navArgs = event.params,
                    )
                }
            }
        }
    }

    GenericPickerScreen(
        title = stringResource("imagevectortoxml.picker.title"),
        description = stringResource("imagevectortoxml.picker.description"),
        fileFilter = { it.toString().endsWith(".kt") },
        onBack = navController::back,
        onFilePick = { path ->
            coroutineScope.launch {
                viewModel.onAction(ImageVectorPickerAction.OnDragAndDropPath(path))
            }
        },
        onTextPaste = { text ->
            viewModel.onAction(ImageVectorPickerAction.OnPasteFromClipboard(text))
        },
        onBrowseClick = {
            coroutineScope.launch {
                filePicker.launch()?.let { path ->
                    viewModel.onAction(ImageVectorPickerAction.OnDragAndDropPath(path))
                }
            }
        },
        onOpenSettings = {
            navController.navigate(dest = SettingsScreen)
        },
    )
}
