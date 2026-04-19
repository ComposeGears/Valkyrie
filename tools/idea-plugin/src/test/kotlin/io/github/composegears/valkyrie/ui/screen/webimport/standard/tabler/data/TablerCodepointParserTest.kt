package io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.data

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class TablerCodepointParserTest {

    @Test
    fun `parse tabler css codepoint`() {
        val parser = TablerCodepointParser()
        val css = """
            .ti-accessible:before { content: "\f6ea" }
            .ti-aerial-lift:before { content: "\10101" }
            .ti-arrow-autofit-down:before { content: "\10113" }
        """.trimIndent()

        val codepoints = parser.parse(css)

        assertThat(codepoints).hasSize(3)
        assertThat(codepoints["accessible"]).isEqualTo(0xF6EA)
        assertThat(codepoints["aerial-lift"]).isEqualTo(0x10101)
        assertThat(codepoints["arrow-autofit-down"]).isEqualTo(0x10113)
    }
}
