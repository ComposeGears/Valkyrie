package io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.data

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText
import org.junit.jupiter.api.Test

class BoxCodepointParserTest {

    @Test
    fun `parse box css codepoint`() {
        val parser = BoxCodepointParser()
        val css = getResourceText("boxicons.min.css")

        val codepoints = parser.parse(css)

        assertThat(codepoints).hasSize(1634)
        assertThat(codepoints["bxs-balloon"]).isEqualTo(0xEB60)
        assertThat(codepoints["bx-child"]).isEqualTo(0xEF48)
        assertThat(codepoints["bxl-baidu"]).isEqualTo(0xE913)
    }
}
