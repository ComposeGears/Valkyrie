package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel

import java.nio.file.Path

sealed interface SimpleConversionAction {

    data object Back : SimpleConversionAction
    data object OpenSettings : SimpleConversionAction
    data object OpenFilePicker : SimpleConversionAction
    data object ClosePreview : SimpleConversionAction
    data class OnPasteFromClipboard(val text: String) : SimpleConversionAction
    data class OnDragAndDropPath(val path: Path) : SimpleConversionAction
    data class OnCopyInClipboard(val text: String) : SimpleConversionAction
    data class OnIconNaneChange(val name: String) : SimpleConversionAction
}
