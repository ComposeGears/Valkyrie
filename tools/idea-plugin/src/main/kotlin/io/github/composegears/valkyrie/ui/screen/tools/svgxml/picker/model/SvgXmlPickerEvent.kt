package io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model

import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlParams

sealed interface SvgXmlPickerEvent {
    data class NavigateToConversion(val params: SvgXmlParams) : SvgXmlPickerEvent
}
