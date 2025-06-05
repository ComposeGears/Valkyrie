package io.github.composegears.valkyrie.generator.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.core.iconpack
import kotlin.test.Test

class IconPackGeneratorTest {

    private fun createConfig(
        iconPack: IconPack = iconpack(name = "ValkyrieIcons"),
        useExplicitMode: Boolean = false,
        indentSize: Int = 4,
    ) = IconPackGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        iconPack = iconPack,
        useExplicitMode = useExplicitMode,
        indentSize = indentSize,
    )

    @Test
    fun `generate icon pack`() {
        val result = IconPackGenerator.create(config = createConfig())

        val expected = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons

        """.trimIndent()

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }

    @Test
    fun `generate icon pack explicit mode`() {
        val result = IconPackGenerator.create(
            config = createConfig(useExplicitMode = true),
        )

        val expected = """
            package io.github.composegears.valkyrie.icons

            public object ValkyrieIcons

        """.trimIndent()

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

        val expected = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons {
                object Rounded {
                    object Filled
                }

                object Sharp {
                    object Colored

                    object Dark
                }
            }

        """.trimIndent()

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

        val expected = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons {
                object Material {
                    object Rounded {
                        object Filled

                        object Outlined
                    }
                }

                object Custom {
                    object Brand
                }
            }

        """.trimIndent()

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

        val expected = """
            package io.github.composegears.valkyrie.icons

            public object ValkyrieIcons {
                public object Filled

                public object Colored
            }

        """.trimIndent()

        assertThat(result).isEqualTo(IconPackSpecOutput(name = "ValkyrieIcons", content = expected))
    }
}
