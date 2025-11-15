package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.model

import java.nio.file.Path

sealed interface ImageVectorPickerAction {
    data class OnPasteFromClipboard(val text: String) : ImageVectorPickerAction
    data class OnDragAndDropPath(val path: Path) : ImageVectorPickerAction
}
