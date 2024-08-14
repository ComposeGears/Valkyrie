package io.github.composegears.valkyrie.cli

import io.github.composegears.valkyrie.extensions.writeToKt
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.parser.isSvg
import io.github.composegears.valkyrie.parser.isXml
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries

internal const val SUCCESS_MESSAGE = "Converting completed."

/**
 * Converts SVG or XML files to ImageVector files.
 */
internal fun res2iv(
    inputPathString: String,
    outputPathString: String,
    packageName: String,
    iconPackName: String,
    nestedPackName: String,
    generatePreview: Boolean,
    outputFormat: OutputFormat,
) {
    val outputPath = Path(outputPathString)
    if (outputPath.isRegularFile()) {
        outputError("The output path must be a directory.")
    } else {
        outputPath.createParentDirectories()
    }

    val inputPath = Path(inputPathString)
    val inputPaths = when {
        inputPath.isDirectory() -> {
            inputPath.listDirectoryEntries("*.{svg,xml}")
                .filter { it.isRegularFile() }
        }
        inputPath.isRegularFile() -> {
            if (!inputPath.isSvg && !inputPath.isXml) {
                outputError("The input file must be an SVG or XML file.")
            }
            listOf(inputPath)
        }
        else -> outputError("The input path is not valid.")
    }

    inputPaths.forEach { path ->
        val parseOutput = IconParser.toVector(path)
        val config = ImageVectorGeneratorConfig(
            packageName = packageName,
            packName = iconPackName,
            nestedPackName = nestedPackName,
            outputFormat = outputFormat,
            generatePreview = generatePreview,
        )
        val vectorSpecOutput = ImageVectorGenerator.convert(
            vector = parseOutput.vector,
            kotlinName = parseOutput.kotlinName,
            config = config,
        )

        vectorSpecOutput.content.writeToKt(
            outputDir = outputPathString,
            nameWithoutExtension = vectorSpecOutput.name,
            createParents = false,
        )
    }

    outputInfo(SUCCESS_MESSAGE)
}
