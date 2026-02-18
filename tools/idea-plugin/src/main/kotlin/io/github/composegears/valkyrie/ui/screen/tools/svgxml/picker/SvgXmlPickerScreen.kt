package io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.TiamatPreview
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.jewel.platform.picker.rememberSvgPathPicker
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.ui.foundation.picker.GenericPickerScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.SvgXmlConversionScreen
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model.SvgXmlPickerAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model.SvgXmlPickerAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model.SvgXmlPickerEvent.NavigateToConversion
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

val SvgXmlPickerScreen by navDestination {

    val navController = navController()
    val coroutineScope = rememberCoroutineScope()
    val filePicker = rememberSvgPathPicker()

    val viewModel = viewModel { SvgXmlPickerViewModel() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is NavigateToConversion -> {
                    navController.navigate(
                        dest = SvgXmlConversionScreen,
                        navArgs = event.params,
                    )
                }
            }
        }
    }

    GenericPickerScreen(
        title = stringResource("svg.to.xml.picker.title"),
        description = stringResource("svg.to.xml.picker.description"),
        fileFilter = { it.toString().endsWith(".svg") },
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
                filePicker.launch()?.let { path ->
                    viewModel.onAction(OnDragAndDropPath(path))
                }
            }
        },
        onOpenSettings = {
            navController.navigate(dest = SettingsScreen)
        },
    )
}

@Preview
@Composable
private fun SvgVectorPickerScreenPreview() = ProjectPreviewTheme {
    TiamatPreview(SvgXmlPickerScreen)
}
