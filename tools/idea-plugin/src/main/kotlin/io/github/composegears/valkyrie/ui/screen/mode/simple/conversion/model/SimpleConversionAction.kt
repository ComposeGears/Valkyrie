package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.model

sealed interface SimpleConversionAction {
    data class OnCopyInClipboard(val text: String) : SimpleConversionAction
    data class OnExport(val text: String) : SimpleConversionAction
    data class OnIconNameChange(val name: String) : SimpleConversionAction
}
