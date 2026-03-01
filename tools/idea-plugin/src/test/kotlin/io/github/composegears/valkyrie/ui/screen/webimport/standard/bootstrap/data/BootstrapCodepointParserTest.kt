package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText
import org.junit.jupiter.api.Test

class BootstrapCodepointParserTest {

    @Test
    fun `parse bootstrap css codepoint`() {
        val parser = BootstrapCodepointParser()
        val css = getResourceText("bootstrap-icons.min.css")

        val codepoints = parser.parse(css)

        assertThat(codepoints).hasSize(2078)
        assertThat(codepoints["123"]).isEqualTo(0xF67F)
        assertThat(codepoints["globe-europe-africa-fill"]).isEqualTo(0xF91E)
    }
}
