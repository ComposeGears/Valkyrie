package io.github.composegears.valkyrie.cli

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.cli.IconPackCommand.IconPackName
import io.github.composegears.valkyrie.cli.IconPackCommand.IndentSize
import io.github.composegears.valkyrie.cli.IconPackCommand.NestedPacks
import io.github.composegears.valkyrie.cli.IconPackCommand.OutputPath
import io.github.composegears.valkyrie.cli.IconPackCommand.PackageName
import io.github.composegears.valkyrie.cli.IconPackCommand.UseExplicitMode
import io.github.composegears.valkyrie.cli.common.CliTestType
import io.github.composegears.valkyrie.cli.common.CliTestType.DirectMain
import io.github.composegears.valkyrie.cli.common.CliTestType.JarTerminal
import io.github.composegears.valkyrie.cli.common.CommandLineTestRunner
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourceText
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.readText
import kotlin.properties.Delegates
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class IconPackCliTest {

    @ParameterizedTest
    @EnumSource(value = CliTestType::class)
    fun `generate icon pack`(cliTestType: CliTestType) {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.kt",
        )
    }

    @ParameterizedTest
    @EnumSource(value = CliTestType::class)
    fun `generate icon pack explicit mode`(cliTestType: CliTestType) {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.explicit.kt",
            useExplicitMode = UseExplicitMode(true),
        )
    }

    @ParameterizedTest
    @EnumSource(value = CliTestType::class)
    fun `generate nested packs`(cliTestType: CliTestType) {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.L2.kt",
            nestedPacks = NestedPacks(listOf("Filled", "Colored")),
        )
    }

    @ParameterizedTest
    @EnumSource(value = CliTestType::class)
    fun `generate nested packs explicit`(cliTestType: CliTestType) {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.explicit.kt",
            nestedPacks = NestedPacks(listOf("Filled", "Colored")),
            useExplicitMode = UseExplicitMode(true),
        )
    }

    @ParameterizedTest
    @EnumSource(value = CliTestType::class)
    fun `generate nested indent 1 packs`(cliTestType: CliTestType) {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.indent1.kt",
            nestedPacks = NestedPacks(listOf("Filled", "Colored")),
            indentSize = IndentSize(1),
        )
    }

    @ParameterizedTest
    @EnumSource(value = CliTestType::class)
    fun `generate nested indent 2 packs`(cliTestType: CliTestType) {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.indent2.kt",
            nestedPacks = NestedPacks(listOf("Filled", "Colored")),
            indentSize = IndentSize(2),
        )
    }

    @ParameterizedTest
    @EnumSource(value = CliTestType::class)
    fun `generate nested indent 3 packs`(cliTestType: CliTestType) {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.indent3.kt",
            nestedPacks = NestedPacks(listOf("Filled", "Colored")),
            indentSize = IndentSize(3),
        )
    }

    @ParameterizedTest
    @EnumSource(value = CliTestType::class)
    fun `generate nested indent 6 packs`(cliTestType: CliTestType) {
        testIconPack(
            cliTestType = cliTestType,
            expectedResource = "iconpack/IconPack.nested.indent6.kt",
            nestedPacks = NestedPacks(listOf("Filled", "Colored")),
            indentSize = IndentSize(6),
        )
    }

    private fun testIconPack(
        cliTestType: CliTestType,
        expectedResource: String,
        useExplicitMode: UseExplicitMode? = null,
        nestedPacks: NestedPacks? = null,
        indentSize: IndentSize? = null,
    ) {
        generateIconPack(
            cliTestType = cliTestType,
            packCommands = listOfNotNull(
                OutputPath(tempDir.absolutePathString()),
                PackageName(name = "io.github.composegears.valkyrie.icons"),
                IconPackName(name = "ValkyrieIcons"),
                useExplicitMode,
                nestedPacks,
                indentSize,
            ),
        )

        val files = tempDir.toFile().listFiles().orEmpty()
        assertThat(files.size).isEqualTo(1)

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

    companion object {
        // Workaround for https://github.com/junit-team/junit5/issues/2811.
        @JvmStatic
        private var tempDir: Path by Delegates.notNull()

        @JvmStatic
        @BeforeAll
        fun before() {
            tempDir = createTempDirectory()
        }

        @JvmStatic
        @AfterAll
        fun after() {
            tempDir.toFile().delete()
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

    data class IconPackName(val name: String) : IconPackCommand {
        override val command: String = "--iconpack-name=$name"
    }

    data class NestedPacks(val packs: List<String>) : IconPackCommand {
        override val command: String = "--nested-packs=${packs.joinToString(separator = ",")}"
    }

    data class IndentSize(val size: Int) : IconPackCommand {
        override val command: String = "--indent-size=$size"
    }

    data class UseExplicitMode(val use: Boolean) : IconPackCommand {
        override val command: String = "--use-explicit-mode=$use"
    }
}
