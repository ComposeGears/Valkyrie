package io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.data

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class EvaCodepointParserTest {

    @Test
    fun `parse eva css codepoint`() {
        val parser = EvaCodepointParser()
        val css = """
            .eva-activity::before {
                content: "\ea01";
            }

            .eva-activity-outline::before {
                content: "\ea02";
            }

            .eva-bell-off-outline::before {
                content: "\ea45";
            }
        """.trimIndent()

        val codepoints = parser.parse(css)
        assertThat(codepoints).hasSize(3)
        assertThat(codepoints["activity"]).isEqualTo(0xEA01)
        assertThat(codepoints["activity-outline"]).isEqualTo(0xEA02)
        assertThat(codepoints["bell-off-outline"]).isEqualTo(0xEA45)
    }
}
