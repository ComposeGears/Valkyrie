package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlParams
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model.ImageVectorXmlPickerAction
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model.ImageVectorXmlPickerAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model.ImageVectorXmlPickerAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model.ImageVectorXmlPickerEvent
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model.ImageVectorXmlPickerEvent.NavigateToConversion
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ImageVectorXmlPickerViewModel : ViewModel() {

    private val _events = Channel<ImageVectorXmlPickerEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: ImageVectorXmlPickerAction) {
        when (action) {
            is OnDragAndDropPath -> navigateToConversion(ImageVectorXmlParams.PathSource(action.path))
            is OnPasteFromClipboard -> navigateToConversion(ImageVectorXmlParams.TextSource(action.text))
        }
    }

    private fun navigateToConversion(params: ImageVectorXmlParams) {
        viewModelScope.launch {
            _events.send(NavigateToConversion(params))
        }
    }
}
