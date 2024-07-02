package io.github.composegears.valkyrie.processing.writter

import com.intellij.openapi.vfs.VirtualFileManager
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.outputStream

object FileWriter {

    fun writeToFile(
        content: String,
        outDirectory: String,
        fileName: String,
    ) {
        val outPath = Path("$outDirectory/$fileName.kt")
        outPath.parent.createDirectories()

        outPath.outputStream().bufferedWriter()
            .use {
                it.write(content)
            }

        VirtualFileManager.getInstance().asyncRefresh()
    }
}