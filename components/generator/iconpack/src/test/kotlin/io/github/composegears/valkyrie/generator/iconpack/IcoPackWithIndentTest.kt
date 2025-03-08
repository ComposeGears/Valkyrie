package io.github.composegears.valkyrie.generator.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourceText
import io.github.composegears.valkyrie.generator.model.iconpack
import org.junit.jupiter.api.Test

class IcoPackWithIndentTest {

    private fun createConfig(
        useExplicitMode: Boolean = false,
        indentSize: Int = 4,
    ) = IconPackGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        iconPack = iconpack(name = "ValkyrieIcons") {
            pack(name = "Filled")
            pack(name = "Colored")
        },
        useExplicitMode = useExplicitMode,
        indentSize = indentSize,
    )

    @Test
    fun `generate nested indent 1 packs`() {
        val result = IconPackGenerator.create(config = createConfig(indentSize = 1))
        val expected = getResourceText("iconpack/IconPack.nested.indent1.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }

    @Test
    fun `generate nested indent 2 packs`() {
        val result = IconPackGenerator.create(config = createConfig(indentSize = 2))
        val expected = getResourceText("iconpack/IconPack.nested.indent2.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }

    @Test
    fun `generate nested indent 3 packs`() {
        val result = IconPackGenerator.create(config = createConfig(indentSize = 3))
        val expected = getResourceText("iconpack/IconPack.nested.indent3.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }

    @Test
    fun `generate nested indent 6 packs`() {
        val result = IconPackGenerator.create(config = createConfig(indentSize = 6))
        val expected = getResourceText("iconpack/IconPack.nested.indent6.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }
}
