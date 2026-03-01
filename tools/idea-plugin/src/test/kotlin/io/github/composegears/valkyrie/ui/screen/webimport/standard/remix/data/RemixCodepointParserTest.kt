package io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.data

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText
import org.junit.jupiter.api.Test

class RemixCodepointParserTest {

    @Test
    fun `parse remix css codepoint`() {
        val parser = RemixCodepointParser()
        val css = getResourceText("remixicon.min.css")

        val codepoints = parser.parse(css)

        assertThat(codepoints).hasSize(3229)
        assertThat(codepoints["24-hours-fill"]).isEqualTo(0xEA01)
        assertThat(codepoints["connector-line"]).isEqualTo(0xF69D)
    }
}
