package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model

sealed interface SimpleConversionEvent {
    data class ExportKtFile(
        val fileName: String,
        val content: String,
    ) : SimpleConversionEvent

    data class CopyInClipboard(val text: String) : SimpleConversionEvent
}
