package io.github.composegears.valkyrie.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.varargValues
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.int
import io.github.composegears.valkyrie.cli.ext.outputInfo
import io.github.composegears.valkyrie.extensions.writeToKt
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig

internal class IconPackCommand : CliktCommand(name = "iconpack") {

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

    private val outputPath by option(
        "--output-path",
        help = "Output path",
    ).required()

    private val packageName by option(
        "--package-name",
        help = "Package name of IconPack object",
    ).required()

    private val iconPackName by option(
        "--iconpack-name",
        help = "IconPack object name",
    ).required()

    private val nestedPacks by option(
        "--nested-packs",
        help = "Nested packs (e.g. Filled, Colored)",
    ).varargValues()

    private val indentSize by option(
        "--indent-size",
        help = "Indent size",
    ).int().default(4)

    private val useExplicitMode by option(
        "--use-explicit-mode",
        help = "Use explicit mode",
    ).boolean().default(false)

    override val printHelpOnEmptyArgs: Boolean = true

    override fun help(context: Context): String = "A CLI tool to generate an IconPack object."

    override fun run() {
        generateIconPack(
            outputPath = outputPath,
            packageName = packageName,
            iconPackName = iconPackName,
            nestedPacks = nestedPacks.orEmpty(),
            useExplicitMode = useExplicitMode,
            indentSize = indentSize,
        )
    }
}

private fun generateIconPack(
    outputPath: String,
    iconPackName: String,
    nestedPacks: List<String>,
    useExplicitMode: Boolean,
    indentSize: Int,
    packageName: String,
) {
    val iconPack = IconPackGenerator.create(
        config = IconPackGeneratorConfig(
            packageName = packageName,
            iconPackName = iconPackName,
            subPacks = nestedPacks,
            useExplicitMode = useExplicitMode,
            indentSize = indentSize,
        ),
    )

    iconPack.content.writeToKt(
        outputDir = outputPath,
        nameWithoutExtension = iconPack.name,
    )

    outputInfo("$iconPackName generated successfully.")
}
