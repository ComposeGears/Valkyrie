package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.jewel.platform.picker.rememberKtPathPicker
import io.github.composegears.valkyrie.ui.foundation.picker.GenericPickerScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.ImageVectorXmlScreen
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model.ImageVectorXmlPickerAction
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model.ImageVectorXmlPickerEvent
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.launch

val ImageVectorXmlPickerScreen by navDestination {
    val navController = navController()
    val coroutineScope = rememberCoroutineScope()
    val filePicker = rememberKtPathPicker()

    val viewModel = viewModel { ImageVectorXmlPickerViewModel() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ImageVectorXmlPickerEvent.NavigateToConversion -> {
                    navController.navigate(
                        dest = ImageVectorXmlScreen,
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
                viewModel.onAction(ImageVectorXmlPickerAction.OnDragAndDropPath(path))
            }
        },
        onTextPaste = { text ->
            viewModel.onAction(ImageVectorXmlPickerAction.OnPasteFromClipboard(text))
        },
        onBrowseClick = {
            coroutineScope.launch {
                filePicker.launch()?.let { path ->
                    viewModel.onAction(ImageVectorXmlPickerAction.OnDragAndDropPath(path))
                }
            }
        },
        onOpenSettings = {
            navController.navigate(dest = SettingsScreen)
        },
    )
}
