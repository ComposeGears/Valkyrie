package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model

sealed interface SvgXmlAction {
    data object OnCopyInClipboard : SvgXmlAction
    data object OnExportFile : SvgXmlAction
    data class OnCodeChange(val newCode: String) : SvgXmlAction
    data class OnNameChange(val name: String) : SvgXmlAction
}
