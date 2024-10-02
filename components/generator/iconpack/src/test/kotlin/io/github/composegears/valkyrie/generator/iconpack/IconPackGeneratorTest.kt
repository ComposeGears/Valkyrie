package io.github.composegears.valkyrie.generator.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourceText
import org.junit.jupiter.api.Test

class IconPackGeneratorTest {

    private fun createConfig(
        subPacks: List<String> = emptyList(),
        useExplicitMode: Boolean = false,
    ) = IconPackGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        iconPackName = "ValkyrieIcons",
        subPacks = subPacks,
        useExplicitMode = useExplicitMode,
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
            config = createConfig(subPacks = listOf("Filled", "Colored")),
        )

        val expected = getResourceText("kt/IconPack.nested.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }

    @Test
    fun `generate nested packs explicit`() {
        val result = IconPackGenerator.create(
            config = createConfig(
                subPacks = listOf("Filled", "Colored"),
                useExplicitMode = true,
            ),
        )

        val expected = getResourceText("kt/IconPack.nested.explicit.kt")

        assertThat(result.content).isEqualTo(expected)
        assertThat(result.name).isEqualTo("ValkyrieIcons")
    }
}
