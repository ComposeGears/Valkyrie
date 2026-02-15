package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model

sealed interface SvgXmlAction {
    data class OnCopyInClipboard(val text: String) : SvgXmlAction
}
