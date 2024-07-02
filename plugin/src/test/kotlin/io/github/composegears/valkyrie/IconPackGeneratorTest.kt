package io.github.composegears.valkyrie

import io.github.composegears.valkyrie.processing.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.processing.generator.iconpack.IconPackGeneratorConfig
import org.junit.Test
import kotlin.test.assertEquals

class IconPackGeneratorTest {

    @Test
    fun `generate icon pack`() {
        val result = IconPackGenerator(
            config = IconPackGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                iconPackName = "ValkyrieIcons",
                subPacks = emptyList()
            )
        ).generate()

        val expectedContent = """
            package io.github.composegears.valkyrie.icons
            
            object ValkyrieIcons
            
        """.trimIndent()

        assertEquals(result.content, expectedContent)
        assertEquals(result.name, "ValkyrieIcons")
    }

    @Test
    fun `generate nested packs`() {
        val result = IconPackGenerator(
            config = IconPackGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                iconPackName = "ValkyrieIcons",
                subPacks = listOf("Filled", "Colored")
            )
        ).generate()

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