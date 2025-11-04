package io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model

import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource

sealed interface SimplePickerEvent {
    data class NavigateToConversion(val params: SimpleConversionParamsSource) : SimplePickerEvent
}
