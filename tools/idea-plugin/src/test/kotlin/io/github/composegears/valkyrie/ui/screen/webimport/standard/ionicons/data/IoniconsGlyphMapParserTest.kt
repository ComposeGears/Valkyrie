package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class IoniconsGlyphMapParserTest {

    @Test
    fun `parse ionicons glyphmap codepoints`() {
        val parser = IoniconsGlyphMapParser()
        val glyphmap = """
            {
              "accessibility": 61696,
              "accessibility-outline": 61697,
              "accessibility-sharp": 61698
            }
        """.trimIndent()

        val codepoints = parser.parse(glyphmap)

        assertThat(codepoints).isEqualTo(
            mapOf(
                "accessibility" to 61696,
                "accessibility-outline" to 61697,
                "accessibility-sharp" to 61698,
            ),
        )
    }
}
