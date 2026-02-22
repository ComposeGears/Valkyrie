package io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlParams
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model.SvgXmlPickerAction
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model.SvgXmlPickerAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model.SvgXmlPickerAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model.SvgXmlPickerEvent
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model.SvgXmlPickerEvent.NavigateToConversion
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SvgXmlPickerViewModel : ViewModel() {

    private val _events = Channel<SvgXmlPickerEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SvgXmlPickerAction) {
        when (action) {
            is OnDragAndDropPath -> navigateToConversion(SvgXmlParams.PathSource(action.path))
            is OnPasteFromClipboard -> navigateToConversion(SvgXmlParams.TextSource(action.text))
        }
    }

    private fun navigateToConversion(params: SvgXmlParams) {
        viewModelScope.launch {
            _events.send(NavigateToConversion(params))
        }
    }
}
