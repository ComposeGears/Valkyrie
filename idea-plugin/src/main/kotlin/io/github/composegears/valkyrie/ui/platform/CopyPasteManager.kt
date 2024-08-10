package io.github.composegears.valkyrie.ui.platform

import com.intellij.openapi.ide.CopyPasteManager
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

fun copyInClipboard(text: String) {
    CopyPasteManager.getInstance().setContents(StringSelection(text))
}

fun pasteFromClipboard(): String? {
    return CopyPasteManager.getInstance().getContents(DataFlavor.stringFlavor)
}
