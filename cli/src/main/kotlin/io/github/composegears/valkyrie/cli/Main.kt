package io.github.composegears.valkyrie.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.int
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat

fun main(vararg args: String) = ValkyrieCommand().main(args.toList())

private class ValkyrieCommand : CliktCommand(help = "A CLI to convert SVG/XML into Compose ImageVector.") {

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

    init {
        versionOption(BuildConfig.VERSION_NAME, names = setOf("-v", "--version"))
    }

    override fun run() {
        res2iv(
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

    private fun stringOption(
        vararg names: String,
        help: String,
        default: String,
    ) = option(names = names, help = help).default(default)

    private fun booleanOption(
        vararg names: String,
        help: String,
        default: Boolean = false,
    ) = option(names = names, help = help).flag(default = default)

    private fun intOption(
        vararg names: String,
        help: String,
        default: Int,
    ) = option(names = names, help = help).int().default(default)

    private fun outputFormatOption() = option(
        "--output-format",
        help = "The command to convert resources to XLS or XLS to resources.",
    ).convert {
        when (it.lowercase()) {
            "backing-property" -> OutputFormat.BackingProperty
            "lazy-property" -> OutputFormat.LazyProperty
            else -> error("Invalid output format, it must be backing-property or lazy-property.")
        }
    }.default(OutputFormat.BackingProperty)
}
