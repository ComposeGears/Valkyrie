package io.github.composegears.valkyrie.generator.jvm.model

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class IconPackDslTest {

    @Test
    fun `simple iconpack`() {
        val iconPack = iconpack(name = "ValkyrieIcons")

        assertThat(iconPack.name).isEqualTo("ValkyrieIcons")
        assertThat(iconPack.nested).hasSize(0)
    }

    @Test
    fun `pack with one level of nesting`() {
        val iconPack = iconpack(name = "ValkyrieIcons") {
            pack(name = "Outlined")
            pack(name = "Filled")
        }

        assertThat(iconPack.name).isEqualTo("ValkyrieIcons")
        assertThat(iconPack.nested).hasSize(2)
        assertThat(iconPack.nested[0].name).isEqualTo("Outlined")
        assertThat(iconPack.nested[1].name).isEqualTo("Filled")
    }

    @Test
    fun `iconpack with multiple nested levels`() {
        val iconPack = iconpack(name = "ValkyrieIcons") {
            pack(name = "Material") {
                pack(name = "Rounded") {
                    pack(name = "Filled")
                }
                pack(name = "Sharp") {
                    pack(name = "Outlined")
                }
            }
            pack(name = "Custom") {
                pack(name = "Brand")
            }
        }

        assertThat(iconPack.name).isEqualTo("ValkyrieIcons")
        assertThat(iconPack.nested).hasSize(2)

        val material = iconPack.nested[0]
        assertThat(material.name).isEqualTo("Material")
        assertThat(material.nested).hasSize(2)

        val rounded = material.nested[0]
        assertThat(rounded.name).isEqualTo("Rounded")
        assertThat(rounded.nested).hasSize(1)
        assertThat(rounded.nested[0].name).isEqualTo("Filled")

        val sharp = material.nested[1]
        assertThat(sharp.name).isEqualTo("Sharp")
        assertThat(sharp.nested).hasSize(1)
        assertThat(sharp.nested[0].name).isEqualTo("Outlined")

        val custom = iconPack.nested[1]
        assertThat(custom.name).isEqualTo("Custom")
        assertThat(custom.nested).hasSize(1)
        assertThat(custom.nested[0].name).isEqualTo("Brand")
    }
}
