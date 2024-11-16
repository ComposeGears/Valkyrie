package io.github.composegears.valkyrie.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.arguments.argument
import io.github.composegears.valkyrie.cli.ext.booleanOption
import io.github.composegears.valkyrie.cli.ext.intOption
import io.github.composegears.valkyrie.cli.ext.outputError
import io.github.composegears.valkyrie.cli.ext.outputFormatOption
import io.github.composegears.valkyrie.cli.ext.outputInfo
import io.github.composegears.valkyrie.cli.ext.stringOption
import io.github.composegears.valkyrie.extensions.writeToKt
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import io.github.composegears.valkyrie.parser.svgxml.util.isSvg
import io.github.composegears.valkyrie.parser.svgxml.util.isXml
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries

internal class SvgXmlToImageVectorCommand : CliktCommand(name = "svgxml2imagevector") {

    init {
        context {
            helpFormatter = {
                MordantHelpFormatter(
                    context = it,
                    requiredOptionMarker = "*",
                    showDefaultValues = true,
                )
            }
        }
    }

    private val inputPath by argument(
        "input-path",
        help = "The directory contains SVG/XML files, it could also be a file path.",
    )
    private val outputPath by argument(
        "output-path",
        help = "The output directory to store the generated sources, it must not be a file path.",
    )

    private val packageName by stringOption(
        "--package-name",
        help = "The package name of the generated sources.",
        default = "com.example",
    )
    private val iconPackName by stringOption(
        "--icon-pack-name",
        help = "The class name of the generated icon pack.",
        default = "Icons",
    )
    private val nestedPackName by stringOption(
        "--nested-pack-name",
        help = "The class name of the generated nested icon pack.",
        default = "",
    )
    private val generatePreview by booleanOption(
        "--generate-preview",
        help = "Generate @Preview.",
    )
    private val outputFormat by outputFormatOption()

    private val useFlatPackage by booleanOption(
        "--flatpackage",
        help = "Export all ImageVector icons into a single package without dividing by nested pack folders.",
    )

    private val useExplicitMode by booleanOption(
        "--explicit-mode",
        help = "Generate ImageVector icons and IconPack with explicit 'public' modifier",
    )

    private val addTrailingComma by booleanOption(
        "--trailing-comma",
        help = "Insert a comma after the last element of ImageVector.Builder block and path params",
    )

    private val indentSize by intOption(
        "--indent-size",
        help = "Determines the number of spaces used for each level of indentation in the exported icon",
        default = 4,
    )

    override val printHelpOnEmptyArgs: Boolean = true

    override fun help(context: Context): String = "A CLI tool to convert SVG/XML into Compose ImageVector."

    override fun run() {
        svgXml2ImageVector(
            inputPathString = inputPath,
            outputPathString = outputPath,
            packageName = packageName,
            iconPackName = iconPackName,
            nestedPackName = nestedPackName,
            generatePreview = generatePreview,
            outputFormat = outputFormat,
            useFlatPackage = useFlatPackage,
            useExplicitMode = useExplicitMode,
            addTrailingComma = addTrailingComma,
            indentSize = indentSize,
        )
    }
}

internal const val SUCCESS_MESSAGE = "Converting completed."

/**
 * Converts SVG or XML files to ImageVector files.
 */
private fun svgXml2ImageVector(
    inputPathString: String,
    outputPathString: String,
    packageName: String,
    iconPackName: String,
    nestedPackName: String,
    generatePreview: Boolean,
    outputFormat: OutputFormat,
    useFlatPackage: Boolean,
    useExplicitMode: Boolean,
    addTrailingComma: Boolean,
    indentSize: Int,
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
        val parseOutput = SvgXmlParser.toIrImageVector(path)
        val config = ImageVectorGeneratorConfig(
            packageName = packageName,
            iconPackPackage = packageName,
            packName = iconPackName,
            nestedPackName = nestedPackName,
            outputFormat = outputFormat,
            generatePreview = generatePreview,
            useFlatPackage = useFlatPackage,
            useExplicitMode = useExplicitMode,
            addTrailingComma = addTrailingComma,
            indentSize = indentSize,
        )
        val vectorSpecOutput = ImageVectorGenerator.convert(
            vector = parseOutput.irImageVector,
            iconName = parseOutput.iconName,
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
