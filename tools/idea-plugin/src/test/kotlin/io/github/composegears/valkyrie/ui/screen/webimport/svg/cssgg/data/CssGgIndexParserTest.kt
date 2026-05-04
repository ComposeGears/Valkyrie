package io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class CssGgIndexParserTest {

    @Test
    fun `parse css gg svg entries from jsdelivr flat index`() {
        val parser = CssGgIndexParser()
        val indexJson = """
            {
              "files": [
                { "name": "/icons/svg/add.svg" },
                { "name": "/icons/svg/arrow-bottom-left.svg" },
                { "name": "/icons/svg/nested/ignored.svg" },
                { "name": "/icons/icons.json" },
                { "name": "/glyphs/glyphs.json" }
              ]
            }
        """.trimIndent()

        val icons = parser.parse(indexJson)

        assertThat(icons.map { it.name }).containsExactly("add", "arrow-bottom-left")
        assertThat(icons.map { it.path }).containsExactly(
            "/icons/svg/add.svg",
            "/icons/svg/arrow-bottom-left.svg",
        )
        assertThat(icons[1].tags).isEqualTo(listOf("arrow", "bottom", "left"))
    }
}
