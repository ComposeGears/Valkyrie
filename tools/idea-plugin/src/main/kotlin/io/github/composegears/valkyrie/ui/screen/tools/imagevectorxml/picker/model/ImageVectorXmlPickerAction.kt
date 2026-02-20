package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.picker.model

import java.nio.file.Path

sealed interface ImageVectorXmlPickerAction {
    data class OnPasteFromClipboard(val text: String) : ImageVectorXmlPickerAction
    data class OnDragAndDropPath(val path: Path) : ImageVectorXmlPickerAction
}
