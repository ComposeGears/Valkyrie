package io.github.composegears.valkyrie.generator.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.core.iconpack
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText
import kotlin.test.Test

class IconPackGeneratorTest {

    private fun createConfig(
        iconPack: IconPack = iconpack(name = "ValkyrieIcons"),
        useExplicitMode: Boolean = false,
        indentSize: Int = 4,
        license: String? = null,
    ) = IconPackGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        iconPack = iconPack,
        useExplicitMode = useExplicitMode,
        indentSize = indentSize,
        license = license,
    )

    @Test
    fun `generate icon pack`() {
        val result = IconPackGenerator.create(config = createConfig())
        val expected = getResourceText("iconpack/IconPack.kt")

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate icon pack explicit mode`() {
        val result = IconPackGenerator.create(config = createConfig(useExplicitMode = true))
        val expected = getResourceText("iconpack/IconPack.explicit.kt")

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate nested pack level 2`() {
        val result = IconPackGenerator.create(
            config = createConfig(
                iconPack = iconpack(name = "ValkyrieIcons") {
                    pack(name = "Filled")
                    pack(name = "Colored")
                },
            ),
        )
        val expected = getResourceText("iconpack/IconPack.nested.L2.kt")

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate nested pack level 3`() {
        val result = IconPackGenerator.create(
            config = createConfig(
                iconPack = iconpack(name = "ValkyrieIcons") {
                    pack(name = "Rounded") {
                        pack(name = "Filled")
                    }
                    pack(name = "Sharp") {
                        pack(name = "Colored")
                        pack(name = "Dark")
                    }
                },
            ),
        )
        val expected = getResourceText("iconpack/IconPack.nested.L3.kt")

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate nested pack level 4`() {
        val result = IconPackGenerator.create(
            config = createConfig(
                iconPack = iconpack(name = "ValkyrieIcons") {
                    pack(name = "Material") {
                        pack(name = "Rounded") {
                            pack(name = "Filled")
                            pack(name = "Outlined")
                        }
                    }
                    pack(name = "Custom") {
                        pack(name = "Brand")
                    }
                },
            ),
        )
        val expected = getResourceText("iconpack/IconPack.nested.L4.kt")

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate nested packs explicit`() {
        val result = IconPackGenerator.create(
            config = createConfig(
                iconPack = iconpack(name = "ValkyrieIcons") {
                    pack(name = "Filled")
                    pack(name = "Colored")
                },
                useExplicitMode = true,
            ),
        )
        val expected = getResourceText("iconpack/IconPack.nested.explicit.kt")

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate icon pack with license`() {
        val license = "/*\n * Copyright (c) 2024 Test\n */"
        val result = IconPackGenerator.create(config = createConfig(license = license))
        val expected = getResourceText("iconpack/IconPack.license.kt")

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate icon pack with license as raw string`() {
        val license = "Copyright (c) 2024 Test"
        val result = IconPackGenerator.create(config = createConfig(license = license))
        val expected = getResourceText("iconpack/IconPack.license.kt")

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }
}
