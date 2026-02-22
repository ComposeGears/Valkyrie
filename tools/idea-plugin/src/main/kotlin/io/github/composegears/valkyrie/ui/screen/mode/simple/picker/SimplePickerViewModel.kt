package io.github.composegears.valkyrie.ui.screen.mode.simple.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.leviathan.compose.inject
import io.github.composegears.valkyrie.sdk.shared.ValkyrieMode
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource.PathSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource.TextSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerAction
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerAction.OnDragAndDropPath
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerAction.OnPasteFromClipboard
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerEvent
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model.SimplePickerEvent.NavigateToConversion
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SimplePickerViewModel : ViewModel() {

    private val inMemorySettings = inject(DI.core.inMemorySettings)

    private val _events = Channel<SimplePickerEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SimplePickerAction) {
        when (action) {
            is OnDragAndDropPath -> navigateToConversion(PathSource(action.path))
            is OnPasteFromClipboard -> navigateToConversion(
                TextSource(
                    name = "IconName",
                    text = action.text,
                ),
            )
        }
    }

    private fun navigateToConversion(params: SimpleConversionParamsSource) {
        viewModelScope.launch {
            updateCurrentMode()
            _events.send(NavigateToConversion(params))
        }
    }

    private fun updateCurrentMode() {
        inMemorySettings.update {
            mode = ValkyrieMode.Simple
        }
    }
}
