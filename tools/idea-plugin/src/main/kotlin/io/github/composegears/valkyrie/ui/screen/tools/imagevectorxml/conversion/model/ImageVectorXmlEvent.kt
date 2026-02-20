package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model

sealed interface ImageVectorXmlEvent {
    data class ExportXmlFile(
        val fileName: String,
        val content: String,
    ) : ImageVectorXmlEvent

    data class CopyInClipboard(val text: String) : ImageVectorXmlEvent
}
