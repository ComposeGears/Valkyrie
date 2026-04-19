package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class CodepointGlyphTest {

    @Test
    fun `convert bmp codepoint to glyph`() {
        assertThat(0xF6EA.toGlyphString()).isEqualTo(String(charArrayOf(0xF6EA.toChar())))
    }

    @Test
    fun `convert supplementary plane codepoint to glyph`() {
        assertThat(0x10113.toGlyphString()).isEqualTo(String(Character.toChars(0x10113)))
    }
}
