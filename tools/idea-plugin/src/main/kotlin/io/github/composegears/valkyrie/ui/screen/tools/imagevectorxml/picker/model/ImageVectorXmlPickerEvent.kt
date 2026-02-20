package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model

import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlParams

sealed interface ImageVectorXmlPickerEvent {
    data class NavigateToConversion(val params: ImageVectorXmlParams) : ImageVectorXmlPickerEvent
}
