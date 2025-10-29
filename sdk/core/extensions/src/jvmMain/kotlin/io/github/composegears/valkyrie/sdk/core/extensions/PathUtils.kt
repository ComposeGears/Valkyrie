package io.github.composegears.valkyrie.sdk.core.extensions

import java.io.IOException
import java.nio.file.Path
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
) = writeToFile(
    outputDir = outputDir,
    nameWithoutExtension = nameWithoutExtension,
    extension = "kt",
    deleteIfExists = deleteIfExists,
    createParents = createParents,
)

@Throws(IOException::class)
private fun String.writeToFile(
    outputDir: String,
    nameWithoutExtension: String,
    extension: String,
    deleteIfExists: Boolean,
    createParents: Boolean,
): Path {
    val outputPath = Path(outputDir, "$nameWithoutExtension.$extension")

    if (deleteIfExists) {
        outputPath.deleteIfExists()
    }
    if (createParents) {
        outputPath.createParentDirectories()
    }
    outputPath.writeText(this)
    return outputPath
}
