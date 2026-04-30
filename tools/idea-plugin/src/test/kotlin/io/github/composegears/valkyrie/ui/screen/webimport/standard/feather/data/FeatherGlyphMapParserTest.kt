package io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class FeatherGlyphMapParserTest {

    @Test
    fun `parse feather glyphmap codepoints`() {
        val parser = FeatherGlyphMapParser()
        val glyphmap = """
            {
              "activity": 61696,
              "arrow-left": 61712
            }
        """.trimIndent()

        val codepoints = parser.parse(glyphmap)
        assertThat(codepoints).isEqualTo(
            mapOf(
                "activity" to 61696,
                "arrow-left" to 61712,
            ),
        )
    }
}
