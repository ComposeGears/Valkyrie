package io.github.composegears.valkyrie.generator.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.generator.core.iconpack
import kotlin.test.Test

class IconPackWithIndentTest {

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
        val expected = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons {
             object Filled

             object Colored
            }

        """.trimIndent()

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate nested indent 2 packs`() {
        val result = IconPackGenerator.create(config = createConfig(indentSize = 2))
        val expected = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons {
              object Filled

              object Colored
            }

        """.trimIndent()

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate nested indent 3 packs`() {
        val result = IconPackGenerator.create(config = createConfig(indentSize = 3))
        val expected = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons {
               object Filled

               object Colored
            }

        """.trimIndent()

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate nested indent 6 packs`() {
        val result = IconPackGenerator.create(config = createConfig(indentSize = 6))
        val expected = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons {
                  object Filled

                  object Colored
            }

        """.trimIndent()

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }
}
