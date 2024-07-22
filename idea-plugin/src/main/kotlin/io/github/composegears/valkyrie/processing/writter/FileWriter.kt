package io.github.composegears.valkyrie.processing.writter

import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.writeText
import kotlin.jvm.Throws

object FileWriter {

    @Throws(IOException::class)
    fun writeToFile(
        content: String,
        outDirectory: String,
        fileName: String,
    ) {
        val outPath = Path("$outDirectory/$fileName.kt")
        outPath.createParentDirectories()
        outPath.writeText(content)
    }
}