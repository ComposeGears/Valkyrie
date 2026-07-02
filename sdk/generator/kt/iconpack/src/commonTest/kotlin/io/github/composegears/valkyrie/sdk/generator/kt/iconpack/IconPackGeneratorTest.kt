package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText
import kotlin.test.Test

class IconPackGeneratorTest {

    private fun createConfig(
        iconPackTree: IconPackTree = buildTree("ValkyrieIcons"),
        useExplicitMode: Boolean = false,
        indentSize: Int = 4,
        license: String? = null,
    ) = IconPackGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        iconPackTree = iconPackTree,
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
                iconPackTree = buildTree("ValkyrieIcons") {
                    child("Filled")
                    child("Colored")
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
                iconPackTree = buildTree("ValkyrieIcons") {
                    child("Rounded") {
                        child("Filled")
                    }
                    child("Sharp") {
                        child("Colored")
                        child("Dark")
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
                iconPackTree = buildTree("ValkyrieIcons") {
                    child("Material") {
                        child("Rounded") {
                            child("Filled")
                            child("Outlined")
                        }
                    }
                    child("Custom") {
                        child("Brand")
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
                iconPackTree = buildTree("ValkyrieIcons") {
                    child("Filled")
                    child("Colored")
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
