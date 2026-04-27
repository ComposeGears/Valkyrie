package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test

class CodepointGlyphTest {

    @Test
    fun `convert bmp codepoint to glyph`() {
        assertThat(Codepoint(0xF6EA).toGlyphString()).isEqualTo("\uF6EA")
    }

    @Test
    fun `convert supplementary plane codepoint to glyph`() {
        assertThat(Codepoint(0x10113).toGlyphString()).isEqualTo("\uD800\uDD13")
    }

    @Test
    fun `invalid codepoint throws exception`() {
        assertFailure { Codepoint(-1) }.isInstanceOf<IllegalArgumentException>()
        assertFailure { Codepoint(Character.MAX_CODE_POINT + 1) }.isInstanceOf<IllegalArgumentException>()
    }
}
