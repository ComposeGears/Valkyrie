package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class HeroiconsIndexParserTest {

    @Test
    fun `parse heroicons svg entries from jsdelivr flat index`() {
        val parser = HeroiconsIndexParser()
        val indexJson = """
            {
              "files": [
                { "name": "/24/outline/academic-cap.svg" },
                { "name": "/24/solid/academic-cap.svg" },
                { "name": "/20/solid/archive-box.svg" },
                { "name": "/README.md" }
              ]
            }
        """.trimIndent()

        val icons = parser.parse(indexJson)

        assertThat(icons.map { it.name }).containsExactly("academic-cap", "academic-cap", "archive-box")
        assertThat(icons.map { it.styleId }).containsExactly("24-outline", "24-solid", "20-solid")
        assertThat(icons.map { it.path }).containsExactly(
            "/24/outline/academic-cap.svg",
            "/24/solid/academic-cap.svg",
            "/20/solid/archive-box.svg",
        )
        assertThat(icons[0].tags).isEqualTo(listOf("academic", "cap"))
    }
}
