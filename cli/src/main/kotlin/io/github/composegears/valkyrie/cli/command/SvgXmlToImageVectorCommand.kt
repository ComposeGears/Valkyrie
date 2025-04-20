package io.github.composegears.valkyrie.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.github.composegears.valkyrie.cli.ext.booleanOption
import io.github.composegears.valkyrie.cli.ext.intOption
import io.github.composegears.valkyrie.cli.ext.outputError
import io.github.composegears.valkyrie.cli.ext.outputInfo
import io.github.composegears.valkyrie.cli.ext.requiredPathOption
import io.github.composegears.valkyrie.cli.ext.requiredStringOption
import io.github.composegears.valkyrie.cli.ext.stringOption
import io.github.composegears.valkyrie.extensions.writeToKt
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import io.github.composegears.valkyrie.parser.svgxml.util.isSvg
import io.github.composegears.valkyrie.parser.svgxml.util.isXml
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

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

    private val inputPath by requiredPathOption(
        "--input-path",
        help = "The directory contains SVG/XML files or path to file",
    )

    private val outputPath by requiredPathOption(
        "--output-path",
        help = "The output directory to store the generated sources",
    )

    private val packageName by requiredStringOption(
        "--package-name",
        help = "The package name of the generated sources (usually equal to IconPack package)",
    )

    private val iconPackName by stringOption(
        "--iconpack-name",
        help = "The name of the existing IconPack",
        default = "",
    )

    private val nestedPackName by stringOption(
        "--nested-pack-name",
        help = "The name of the existing nested IconPack",
        default = "",
    )

    private val outputFormat by outputFormatOption()

    private val useComposeColors by booleanOption(
        "--use-compose-colors",
        default = true,
        help = "Use predefined Compose colors instead of hex color codes (e.g. Color.Black instead of Color(0xFF000000))",
    )

    private val generatePreview by booleanOption(
        "--generate-preview",
        help = "Generate @Preview",
    )

    private val previewAnnotationType by previewAnnotationType()

    private val useFlatPackage by booleanOption(
        "--flatpackage",
        help = "Export all icons into a single package without dividing by nested pack folders",
    )

    private val useExplicitMode by booleanOption(
        "--explicit-mode",
        help = "Add explicit 'public' modifier",
    )

    private val addTrailingComma by booleanOption(
        "--trailing-comma",
        help = "Insert a comma after the last element of ImageVector.Builder block and path params",
    )

    private val indentSize by intOption(
        "--indent-size",
        help = "Determines the number of spaces used for each level of indentation in the icon",
        default = 4,
    )

    private val verbose by option(
        "-v",
        "--verbose",
        help = "Print additional info logs",
    ).flag()

    override val printHelpOnEmptyArgs: Boolean = true

    override fun help(context: Context): String = "A CLI tool to convert SVG/XML into Compose ImageVector."

    override fun run() {
        svgXml2ImageVector(
            inputPath = inputPath,
            outputPath = outputPath,
            packageName = packageName,
            iconPackName = iconPackName,
            nestedPackName = nestedPackName,
            generatePreview = generatePreview,
            previewAnnotationType = previewAnnotationType,
            outputFormat = outputFormat,
            useComposeColors = useComposeColors,
            useFlatPackage = useFlatPackage,
            useExplicitMode = useExplicitMode,
            addTrailingComma = addTrailingComma,
            indentSize = indentSize,
            verbose = verbose,
        )
    }
}

private fun CliktCommand.outputFormatOption() = option(
    "--output-format",
    help = "ImageVector output format, must be 'backing-property' or 'lazy-property'",
).convert {
    when (it.lowercase()) {
        "backing-property" -> OutputFormat.BackingProperty
        "lazy-property" -> OutputFormat.LazyProperty
        else -> error("Invalid output format, must be backing-property or lazy-property")
    }
}.default(OutputFormat.BackingProperty)

private fun CliktCommand.previewAnnotationType() = option(
    "--preview-annotation-type",
    help = "Specifies the type of Preview annotation, must be 'androidx' or 'jetbrains'",
).convert {
    when (it.lowercase()) {
        "androidx" -> PreviewAnnotationType.AndroidX
        "jetbrains" -> PreviewAnnotationType.Jetbrains
        else -> error("Invalid preview annotation type, must be 'androidx' or 'jetbrains'")
    }
}.default(PreviewAnnotationType.AndroidX)

/**
 * Converts SVG or XML files to ImageVector files.
 */
private fun svgXml2ImageVector(
    inputPath: Path,
    outputPath: Path,
    packageName: String,
    iconPackName: String,
    nestedPackName: String,
    generatePreview: Boolean,
    previewAnnotationType: PreviewAnnotationType,
    outputFormat: OutputFormat,
    useComposeColors: Boolean,
    useFlatPackage: Boolean,
    useExplicitMode: Boolean,
    addTrailingComma: Boolean,
    indentSize: Int,
    verbose: Boolean,
) {
    val iconPaths = when {
        inputPath.isDirectory() -> {
            inputPath
                .listDirectoryEntries("*.{svg,xml}")
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

    if (iconPaths.isEmpty()) {
        outputError("No any icons to process")
    }

    if (outputPath.isRegularFile()) {
        outputError("The output path must be a directory.")
    }

    outputInfo("Start processing...")

    iconPaths
        .sortedBy { it.name }
        .forEach { path ->
            if (verbose) {
                outputInfo("process = $path")
            }
            val parseOutput = SvgXmlParser.toIrImageVector(path)
            val config = ImageVectorGeneratorConfig(
                packageName = packageName,
                iconPackPackage = packageName,
                packName = iconPackName,
                nestedPackName = nestedPackName,
                outputFormat = outputFormat,
                useComposeColors = useComposeColors,
                generatePreview = generatePreview,
                previewAnnotationType = previewAnnotationType,
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
                outputDir = when {
                    useFlatPackage -> outputPath.absolutePathString()
                    else -> "${outputPath.absolutePathString()}/${nestedPackName.lowercase()}"
                },
                nameWithoutExtension = vectorSpecOutput.name,
            )
        }

    outputInfo("Successfully converted ${iconPaths.size} icons")
}
