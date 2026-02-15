package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlParams
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model.ImageVectorPickerAction
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model.ImageVectorPickerAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model.ImageVectorPickerAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model.ImageVectorPickerEvent
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model.ImageVectorPickerEvent.NavigateToConversion
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ImageVectorPickerViewModel : ViewModel() {

    private val _events = MutableSharedFlow<ImageVectorPickerEvent>()
    val events = _events.asSharedFlow()

    fun onAction(action: ImageVectorPickerAction) {
        when (action) {
            is OnDragAndDropPath -> navigateToConversion(ImageVectorToXmlParams.PathSource(action.path))
            is OnPasteFromClipboard -> navigateToConversion(ImageVectorToXmlParams.TextSource(action.text))
        }
    }

    private fun navigateToConversion(params: ImageVectorToXmlParams) {
        viewModelScope.launch {
            _events.emit(NavigateToConversion(params))
        }
    }
}
