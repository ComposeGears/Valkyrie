package io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.data

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText
import org.junit.jupiter.api.Test

class LucideCodepointParserTest {

    @Test
    fun `parse lucide css codepoint`() {
        val parser = LucideCodepointParser()
        val css = getResourceText("lucide.css")

        val codepoints = parser.parse(css)

        assertThat(codepoints).hasSize(1688)
        assertThat(codepoints["a-arrow-down"]).isEqualTo(0xE585)
        assertThat(codepoints["zoom-out"]).isEqualTo(0xE1B7)
    }
}
