package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model

import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlParams

sealed interface ImageVectorPickerEvent {
    data class NavigateToConversion(val params: ImageVectorToXmlParams) : ImageVectorPickerEvent
}
