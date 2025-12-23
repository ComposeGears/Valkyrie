package io.github.composegears.valkyrie.cli

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.cli.IconPackCommand.IconPackStructure
import io.github.composegears.valkyrie.cli.IconPackCommand.IndentSize
import io.github.composegears.valkyrie.cli.IconPackCommand.OutputPath
import io.github.composegears.valkyrie.cli.IconPackCommand.PackageName
import io.github.composegears.valkyrie.cli.IconPackCommand.UseExplicitMode
import io.github.composegears.valkyrie.cli.common.CliTestType
import io.github.composegears.valkyrie.cli.common.CliTestType.DirectMain
import io.github.composegears.valkyrie.cli.common.CliTestType.JarTerminal
import io.github.composegears.valkyrie.cli.common.CommandLineTestRunner
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.readText
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedClass
import org.junit.jupiter.params.provider.EnumSource

@ParameterizedClass
@EnumSource(value = CliTestType::class)
class IconPackCliTest(
    private val cliTestType: CliTestType,
) {
    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `generate icon pack`() {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.kt",
            iconPack = IconPackStructure("ValkyrieIcons"),
        )
    }

    @Test
    fun `generate icon pack explicit mode`() {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.explicit.kt",
            iconPack = IconPackStructure("ValkyrieIcons"),
            useExplicitMode = UseExplicitMode(true),
        )
    }

    @Test
    fun `generate nested packs`() {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.L2.kt",
            iconPack = IconPackStructure("ValkyrieIcons.Filled,ValkyrieIcons.Colored"),
        )
    }

    @Test
    fun `generate nested packs explicit`() {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.explicit.kt",
            iconPack = IconPackStructure("ValkyrieIcons.Filled,ValkyrieIcons.Colored"),
            useExplicitMode = UseExplicitMode(true),
        )
    }

    @Test
    fun `generate nested indent 1 packs`() {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.indent1.kt",
            iconPack = IconPackStructure("ValkyrieIcons.Filled,ValkyrieIcons.Colored"),
            indentSize = IndentSize(1),
        )
    }

    @Test
    fun `generate nested indent 2 packs`() {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.indent2.kt",
            iconPack = IconPackStructure("ValkyrieIcons.Filled,ValkyrieIcons.Colored"),
            indentSize = IndentSize(2),
        )
    }

    @Test
    fun `generate nested indent 3 packs`() {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.indent3.kt",
            iconPack = IconPackStructure("ValkyrieIcons.Filled,ValkyrieIcons.Colored"),
            indentSize = IndentSize(3),
        )
    }

    @Test
    fun `generate nested indent 6 packs`() {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.indent6.kt",
            iconPack = IconPackStructure("ValkyrieIcons.Filled,ValkyrieIcons.Colored"),
            indentSize = IndentSize(6),
        )
    }

    private fun testIconPack(
        cliTestType: CliTestType,
        expectedResource: String,
        iconPack: IconPackStructure,
        useExplicitMode: UseExplicitMode? = null,
        indentSize: IndentSize? = null,
    ) {
        generateIconPack(
            cliTestType = cliTestType,
            packCommands = listOfNotNull(
                OutputPath(tempDir.absolutePathString()),
                PackageName(name = "io.github.composegears.valkyrie.icons"),
                iconPack,
                useExplicitMode,
                indentSize,
            ),
        )

        assertThat(tempDir.listDirectoryEntries().size).isEqualTo(1)

        val result = tempDir.resolve("ValkyrieIcons.kt").readText()
        val expected = getResourceText(expectedResource)
        assertThat(result).isEqualTo(expected)
    }

    private fun generateIconPack(cliTestType: CliTestType, packCommands: List<IconPackCommand>) {
        val commands = listOf("iconpack") + packCommands.map(IconPackCommand::command)

        when (cliTestType) {
            DirectMain -> main(*commands.toTypedArray())
            JarTerminal -> CommandLineTestRunner(commands).run()
        }
    }
}

private sealed interface IconPackCommand {
    val command: String

    data class OutputPath(val path: String) : IconPackCommand {
        override val command: String = "--output-path=$path"
    }

    data class PackageName(val name: String) : IconPackCommand {
        override val command: String = "--package-name=$name"
    }

    data class IconPackStructure(val value: String) : IconPackCommand {
        override val command: String = "--iconpack=$value"
    }

    data class IndentSize(val size: Int) : IconPackCommand {
        override val command: String = "--indent-size=$size"
    }

    data class UseExplicitMode(val use: Boolean) : IconPackCommand {
        override val command: String = "--use-explicit-mode=$use"
    }
}
