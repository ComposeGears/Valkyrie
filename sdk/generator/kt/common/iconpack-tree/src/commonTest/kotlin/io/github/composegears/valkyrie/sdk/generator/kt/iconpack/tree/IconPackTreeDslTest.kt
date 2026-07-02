package io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import kotlin.test.Test

class IconPackTreeDslTest {

    @Test
    fun `simple iconpack`() {
        val iconPack = buildTree("ValkyrieIcons")

        assertThat(iconPack.data).isEqualTo("ValkyrieIcons")
        assertThat(iconPack.children).hasSize(0)
    }

    @Test
    fun `pack with one level of nesting`() {
        val iconPack = buildTree("ValkyrieIcons") {
            child("Outlined")
            child("Filled")
        }

        assertThat(iconPack.data).isEqualTo("ValkyrieIcons")
        assertThat(iconPack.children).hasSize(2)
        assertThat(iconPack.children[0].data).isEqualTo("Outlined")
        assertThat(iconPack.children[1].data).isEqualTo("Filled")
    }

    @Test
    fun `iconpack with multiple nested levels`() {
        val iconPack = buildTree("ValkyrieIcons") {
            child("Material") {
                child("Rounded") {
                    child("Filled")
                }
                child("Sharp") {
                    child("Outlined")
                }
            }
            child("Custom") {
                child("Brand")
            }
        }

        assertThat(iconPack.data).isEqualTo("ValkyrieIcons")
        assertThat(iconPack.children).hasSize(2)

        val material = iconPack.children[0]
        assertThat(material.data).isEqualTo("Material")
        assertThat(material.children).hasSize(2)

        val rounded = material.children[0]
        assertThat(rounded.data).isEqualTo("Rounded")
        assertThat(rounded.children).hasSize(1)
        assertThat(rounded.children[0].data).isEqualTo("Filled")

        val sharp = material.children[1]
        assertThat(sharp.data).isEqualTo("Sharp")
        assertThat(sharp.children).hasSize(1)
        assertThat(sharp.children[0].data).isEqualTo("Outlined")

        val custom = iconPack.children[1]
        assertThat(custom.data).isEqualTo("Custom")
        assertThat(custom.children).hasSize(1)
        assertThat(custom.children[0].data).isEqualTo("Brand")
    }
}
