package io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.model

import java.nio.file.Path

sealed interface SvgXmlPickerAction {
    data class OnPasteFromClipboard(val text: String) : SvgXmlPickerAction
    data class OnDragAndDropPath(val path: Path) : SvgXmlPickerAction
}
