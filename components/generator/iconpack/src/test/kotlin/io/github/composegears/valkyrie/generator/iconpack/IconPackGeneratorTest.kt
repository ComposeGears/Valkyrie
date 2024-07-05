package io.github.composegears.valkyrie.generator.iconpack

import org.junit.Test
import kotlin.test.assertEquals

class IconPackGeneratorTest {

    @Test
    fun `generate icon pack`() {
        val result = IconPackGenerator.create(
            config = IconPackGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                iconPackName = "ValkyrieIcons",
                subPacks = emptyList()
            )
        )

        val expectedContent = """
            package io.github.composegears.valkyrie.icons
            
            object ValkyrieIcons
            
        """.trimIndent()

        assertEquals(result.content, expectedContent)
        assertEquals(result.name, "ValkyrieIcons")
    }

    @Test
    fun `generate nested packs`() {
        val result = IconPackGenerator.create(
            config = IconPackGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                iconPackName = "ValkyrieIcons",
                subPacks = listOf("Filled", "Colored")
            )
        )

        val expectedContent = """
            package io.github.composegears.valkyrie.icons
            
            object ValkyrieIcons {
                object Filled

                object Colored
            }
            
        """.trimIndent()

        assertEquals(result.content, expectedContent)
        assertEquals(result.name, "ValkyrieIcons")
    }
}