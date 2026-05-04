package io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class OcticonsMetadataParserTest {

    @Test
    fun `parse octicons metadata entries from package data`() {
        val parser = OcticonsMetadataParser()
        val metadataJson = """
            {
              "alert": {
                "name": "alert",
                "keywords": ["warning", "danger"],
                "heights": {
                  "16": { "width": 16 },
                  "24": { "width": 24 }
                }
              },
              "arrow-left": {
                "name": "arrow-left",
                "keywords": [],
                "heights": {
                  "24": { "width": 24 }
                }
              }
            }
        """.trimIndent()

        val icons = parser.parse(metadataJson)

        assertThat(icons.map { it.name }).containsExactly("alert", "alert", "arrow-left")
        assertThat(icons.map { it.size }).containsExactly("16", "24", "24")
        assertThat(icons.map { it.width }).containsExactly(16, 24, 24)
        assertThat(icons.map { it.path }).containsExactly("alert-16.svg", "alert-24.svg", "arrow-left-24.svg")
        assertThat(icons[0].tags).isEqualTo(listOf("alert", "warning", "danger"))
        assertThat(icons[2].tags).isEqualTo(listOf("arrow", "left"))
    }
}
