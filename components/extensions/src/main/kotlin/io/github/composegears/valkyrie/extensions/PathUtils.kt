package io.github.composegears.valkyrie.extensions

import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

@Throws(IOException::class)
fun String.writeToKt(
    outputDir: String,
    nameWithoutExtension: String,
    deleteIfExists: Boolean = true,
    createParents: Boolean = true,
) {
    val outputPath = Path(outputDir, "$nameWithoutExtension.kt")

    if (deleteIfExists) {
        outputPath.deleteIfExists()
    }
    if (createParents) {
        outputPath.createParentDirectories()
    }
    outputPath.writeText(this)
}
