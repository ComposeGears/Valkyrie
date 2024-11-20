package io.github.composegears.valkyrie.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import io.github.composegears.valkyrie.cli.ext.booleanOption
import io.github.composegears.valkyrie.cli.ext.intOption
import io.github.composegears.valkyrie.cli.ext.outputInfo
import io.github.composegears.valkyrie.cli.ext.requiredPathOption
import io.github.composegears.valkyrie.cli.ext.requiredStringOption
import io.github.composegears.valkyrie.cli.ext.stringOption
import io.github.composegears.valkyrie.extensions.writeToKt
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import java.nio.file.Path
import kotlin.io.path.absolutePathString

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

    private val outputPath by requiredPathOption(
        "--output-path",
        help = "Output path",
    )

    private val packageName by requiredStringOption(
        "--package-name",
        help = "Package name of IconPack object",
    )

    private val iconPackName by stringOption(
        "--iconpack-name",
        help = "IconPack object name",
        default = "",
    )

    private val nestedPacks by option(
        "--nested-packs",
        help = "Nested packs (e.g. Filled, Colored)",
    ).split(",")

    private val indentSize by intOption(
        "--indent-size",
        help = "Indent size",
        default = 4,
    )

    private val useExplicitMode by booleanOption(
        "--use-explicit-mode",
        help = "Use explicit mode",
    )

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
    outputPath: Path,
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
            nestedPacks = nestedPacks,
            useExplicitMode = useExplicitMode,
            indentSize = indentSize,
        ),
    )

    iconPack.content.writeToKt(
        outputDir = outputPath.absolutePathString(),
        nameWithoutExtension = iconPack.name,
    )

    outputInfo("$iconPackName generated successfully.")
}
