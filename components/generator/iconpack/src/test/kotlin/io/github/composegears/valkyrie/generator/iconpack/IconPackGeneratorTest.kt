package io.github.composegears.valkyrie.generator.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourceText
import org.junit.jupiter.api.Test

class IconPackGeneratorTest {

    private fun createConfig(
        nestedPacks: List<String> = emptyList(),
        useExplicitMode: Boolean = false,
        indentSize: Int = 4,
    ) = IconPackGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        iconPackName = "ValkyrieIcons",
        nestedPacks = nestedPacks,
        useExplicitMode = useExplicitMode,
        indentSize = indentSize,
    )

    @Test
    fun `generate icon pack`() {
        val result = IconPackGenerator.create(config = createConfig())

        val expected = getResourceText("kt/IconPack.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }

    @Test
    fun `generate icon pack explicit mode`() {
        val result = IconPackGenerator.create(
            config = createConfig(useExplicitMode = true),
        )

        val expected = getResourceText("kt/IconPack.explicit.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }

    @Test
    fun `generate nested packs`() {
        val result = IconPackGenerator.create(
            config = createConfig(nestedPacks = listOf("Filled", "Colored")),
        )

        val expected = getResourceText("kt/IconPack.nested.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }

    @Test
    fun `generate nested packs explicit`() {
        val result = IconPackGenerator.create(
            config = createConfig(
                nestedPacks = listOf("Filled", "Colored"),
                useExplicitMode = true,
            ),
        )

        val expected = getResourceText("kt/IconPack.nested.explicit.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }
}
