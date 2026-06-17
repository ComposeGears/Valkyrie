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
import io.github.composegears.valkyrie.cli.ext.booleanOrNullOption
import io.github.composegears.valkyrie.cli.ext.intOption
import io.github.composegears.valkyrie.cli.ext.outputError
import io.github.composegears.valkyrie.cli.ext.outputInfo
import io.github.composegears.valkyrie.cli.ext.requiredPathOption
import io.github.composegears.valkyrie.cli.ext.requiredStringOption
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.isSvg
import io.github.composegears.valkyrie.parser.unified.ext.isXml
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.core.extensions.writeToKt
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.iconPackOf
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.pathSegments
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.CodeStyleConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.FullyQualifiedImports
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.FullyQualifiedImports.Companion.reservedComposeTypeNames
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.ImageVectorGenerator
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

    private val iconPackTree by option(
        "--iconpack",
        help = "Icon pack structure (e.g. 'ValkyrieIcons' or 'ValkyrieIcons.Filled')",
    ).convert { iconPackOf(it) }

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

    private val useFlatPackage by booleanOption(
        "--flatpackage",
        help = "Import all icons into a single package without dividing by nested pack folders",
    )

    private val useExplicitMode by booleanOption(
        "--explicit-mode",
        help = "Add explicit 'public' modifier",
    )

    private val addTrailingComma by booleanOption(
        "--trailing-comma",
        help = "Insert a comma after the last element of ImageVector.Builder block and path params",
    )

    private val usePathDataString by booleanOption(
        "--use-path-data-string",
        help = "Generate addPath with pathData strings instead of path builder calls (less optimal due to pre-rendering parsing)",
    )

    private val suppressUnusedReceiverWarning by booleanOption(
        "--suppress-unused-receiver",
        help = "Add @Suppress(\"UnusedReceiverParameter\") annotation to generated ImageVector extension properties",
    )

    private val indentSize by intOption(
        "--indent-size",
        help = "Determines the number of spaces used for each level of indentation in the icon",
        default = 4,
    )

    private val autoMirror by booleanOrNullOption(
        "--auto-mirror",
        help = "Force add \"autoMirror=true\" or \"autoMirror=false\" option to generated ImageVector",
    )

    private val verbose by option(
        "-v",
        "--verbose",
        help = "Print additional info logs",
    ).flag()

    override val printHelpOnEmptyArgs: Boolean = true

    override fun help(context: Context): String = "A CLI tool to convert SVG/XML into Compose ImageVector"

    override fun run() {
        svgXml2ImageVector(
            inputPath = inputPath,
            outputPath = outputPath,
            packageName = packageName,
            iconPackTree = iconPackTree,
            generatePreview = generatePreview,
            outputFormat = outputFormat,
            useComposeColors = useComposeColors,
            useFlatPackage = useFlatPackage,
            useExplicitMode = useExplicitMode,
            addTrailingComma = addTrailingComma,
            usePathDataString = usePathDataString,
            suppressUnusedReceiverWarning = suppressUnusedReceiverWarning,
            indentSize = indentSize,
            autoMirror = autoMirror,
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
        else -> outputError("Invalid output format, must be backing-property or lazy-property")
    }
}.default(OutputFormat.BackingProperty)

/**
 * Converts SVG or XML files to ImageVector files.
 */
private fun svgXml2ImageVector(
    inputPath: Path,
    outputPath: Path,
    packageName: String,
    iconPackTree: IconPackTree?,
    generatePreview: Boolean,
    outputFormat: OutputFormat,
    useComposeColors: Boolean,
    useFlatPackage: Boolean,
    useExplicitMode: Boolean,
    addTrailingComma: Boolean,
    usePathDataString: Boolean,
    suppressUnusedReceiverWarning: Boolean,
    indentSize: Int,
    autoMirror: Boolean?,
    verbose: Boolean,
) {
    val iconPaths = when {
        inputPath.isDirectory() -> {
            inputPath
                .listDirectoryEntries("*.{svg,xml}")
                .filter { it.isRegularFile() }
        }
        inputPath.isRegularFile() -> {
            if (!inputPath.toIOPath().isSvg && !inputPath.toIOPath().isXml) {
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

    val iconNames = iconPaths.map { IconNameFormatter.format(name = it.name) }

    val fullyQualifiedNames = iconNames.filter { it in reservedComposeTypeNames }

    if (fullyQualifiedNames.isNotEmpty()) {
        outputInfo(
            "Found icons names that conflict with reserved Compose qualifiers. " +
                "Full qualified import will be used for: ${fullyQualifiedNames.joinToString(", ")}",
        )
    }

    // Check for exact duplicates
    val exactDuplicates = iconNames
        .groupBy { it }
        .filter { it.value.size > 1 }
        .keys
        .toList()
        .sorted()

    if (exactDuplicates.isNotEmpty()) {
        outputError(
            "Found duplicate icon names: ${exactDuplicates.joinToString(", ")}. " +
                "Each icon must have a unique name. " +
                "Please rename the source files to avoid duplicates.",
        )
    }

    // Check for case-insensitive duplicates that would cause file overwrites on case-insensitive file systems
    val caseInsensitiveDuplicates = iconNames
        .groupBy { it.lowercase() }
        .filter { it.value.size > 1 && it.value.distinct().size > 1 }
        .values
        .flatten()
        .distinct()
        .sorted()

    if (caseInsensitiveDuplicates.isNotEmpty()) {
        outputError(
            "Found icon names that would collide on case-insensitive file systems (macOS/Windows): " +
                "${caseInsensitiveDuplicates.joinToString(", ")}. " +
                "These icons would overwrite each other during generation. " +
                "Please rename the source files to avoid case-insensitive duplicates.",
        )
    }

    val codeStyle = CodeStyleConfig(
        useExplicitMode = useExplicitMode,
        indentSize = indentSize,
    )
    val imageVector = ImageVectorConfig(
        outputFormat = outputFormat,
        useComposeColors = useComposeColors,
        generatePreview = generatePreview,
        useFlatPackage = useFlatPackage,
        addTrailingComma = addTrailingComma,
        usePathDataString = usePathDataString,
        suppressUnusedReceiverWarning = suppressUnusedReceiverWarning,
    )
    val fullyQualifiedImports = FullyQualifiedImports.from(fullyQualifiedNames)
    val config = when {
        iconPackTree != null -> {
            ImageVectorGeneratorConfig.iconPack(
                iconName = "",
                packageName = packageName,
                iconPackPackage = packageName,
                iconPackTree = iconPackTree,
                codeStyle = codeStyle,
                imageVector = imageVector,
                fullyQualifiedImports = fullyQualifiedImports,
            )
        }
        else -> {
            ImageVectorGeneratorConfig.simple(
                iconName = "",
                packageName = packageName,
                codeStyle = codeStyle,
                imageVector = imageVector,
                fullyQualifiedImports = fullyQualifiedImports,
            )
        }
    }

    iconPaths
        .sortedBy { it.name }
        .forEach { path ->
            if (verbose) {
                outputInfo("process = $path")
            }
            val parseOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = path.toIOPath()).run {
                when {
                    autoMirror != null -> copy(irImageVector = irImageVector.copy(autoMirror = autoMirror))
                    else -> this
                }
            }

            val vectorSpecOutput = ImageVectorGenerator.convert(
                vector = parseOutput.irImageVector,
                config = config.copy(iconName = parseOutput.iconName),
            )

            vectorSpecOutput.content.writeToKt(
                outputDir = when {
                    useFlatPackage || iconPackTree == null -> outputPath
                    else -> {
                        val subDir = iconPackTree.pathSegments().drop(1).joinToString("/") { it.lowercase() }
                        if (subDir.isNotEmpty()) outputPath.resolve(subDir) else outputPath
                    }
                }.absolutePathString(),
                nameWithoutExtension = vectorSpecOutput.name,
            )
        }

    val count = iconPaths.size
    val noun = if (count == 1) "icon" else "icons"
    outputInfo("Successfully converted $count $noun")
}
