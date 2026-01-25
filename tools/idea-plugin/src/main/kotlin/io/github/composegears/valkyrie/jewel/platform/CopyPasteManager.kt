package io.github.composegears.valkyrie.jewel.platform

import com.intellij.openapi.ide.CopyPasteManager
import io.github.composegears.valkyrie.sdk.core.extensions.cast
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.io.File
import java.nio.file.Path

fun copyInClipboard(text: String) {
    CopyPasteManager.getInstance().setContents(StringSelection(text))
}

fun pasteFromClipboard(): ClipboardDataType? {
    val clipboardContents = CopyPasteManager.getInstance().contents ?: return null

    if (clipboardContents.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        val files = clipboardContents
            .getTransferData(DataFlavor.javaFileListFlavor)
            .cast<List<*>>()
            .filterIsInstance<File>()
            .map(File::toPath)

        if (files.isNotEmpty()) {
            return ClipboardDataType.Files(files)
        }
    }

    if (clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        val text = clipboardContents.getTransferData(DataFlavor.stringFlavor).cast<String>()
        return ClipboardDataType.Text(text)
    }

    return null
}

sealed interface ClipboardDataType {
    data class Text(val text: String) : ClipboardDataType
    data class Files(val paths: List<Path>) : ClipboardDataType
}
