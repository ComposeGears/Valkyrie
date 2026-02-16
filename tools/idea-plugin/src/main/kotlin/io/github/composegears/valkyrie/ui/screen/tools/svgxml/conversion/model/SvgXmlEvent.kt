package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model

sealed interface SvgXmlEvent {
    data class ExportXmlFile(
        val fileName: String,
        val content: String,
    ) : SvgXmlEvent

    data class CopyInClipboard(val text: String) : SvgXmlEvent
}
