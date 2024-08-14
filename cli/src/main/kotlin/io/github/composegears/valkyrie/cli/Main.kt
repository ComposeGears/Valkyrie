package io.github.composegears.valkyrie.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat

fun main(vararg args: String) = ValkyrieCommand().main(args.toList())

private class ValkyrieCommand :
    CliktCommand(
        help = "A CLI to convert SVG/XML into Compose ImageVector.",
    ) {
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
