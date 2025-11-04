package io.github.composegears.valkyrie.ui.screen.mode.simple.picker.model

import java.nio.file.Path

sealed interface SimplePickerAction {
    data class OnPasteFromClipboard(val text: String) : SimplePickerAction
    data class OnDragAndDropPath(val path: Path) : SimplePickerAction
}
