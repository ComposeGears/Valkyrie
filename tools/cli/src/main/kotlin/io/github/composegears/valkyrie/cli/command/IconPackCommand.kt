package io.github.composegears.valkyrie.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.MordantHelpFormatter
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import io.github.composegears.valkyrie.cli.ext.booleanOption
import io.github.composegears.valkyrie.cli.ext.intOption
import io.github.composegears.valkyrie.cli.ext.outputInfo
import io.github.composegears.valkyrie.cli.ext.requiredPathOption
import io.github.composegears.valkyrie.cli.ext.requiredStringOption
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.sdk.core.extensions.writeToKt
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

    private val iconPack by option(
        "--iconpack",
        help = "Simple or hierarchical icon pack structure (e.g. 'MyIconPack' or 'MyIconPack.Filled,MyIconPack.Outlined')",
    ).convert {
        IconPack.fromString(it)
    }.default(IconPack(name = ""))

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
            iconPack = iconPack,
            useExplicitMode = useExplicitMode,
            indentSize = indentSize,
        )
    }
}

private fun generateIconPack(
    outputPath: Path,
    iconPack: IconPack,
    useExplicitMode: Boolean,
    indentSize: Int,
    packageName: String,
) {
    IconPackGenerator.create(
        config = IconPackGeneratorConfig(
            packageName = packageName,
            iconPack = iconPack,
            useExplicitMode = useExplicitMode,
            indentSize = indentSize,
        ),
    ).also {
        it.content.writeToKt(
            outputDir = outputPath.absolutePathString(),
            nameWithoutExtension = it.name,
        )

        outputInfo("${it.name} generated successfully.")
    }
}
