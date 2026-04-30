package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class IoniconsMetadataParserTest {

    @Test
    fun `parse ionicons metadata tags`() {
        val parser = IoniconsMetadataParser()
        val metadata = """
            {
              "name": "ionicons",
              "icons": [
                { "name": "accessibility", "tags": ["accessibility"] },
                { "name": "add-circle-outline", "tags": ["add", "circle", "outline", "plus"] }
              ]
            }
        """.trimIndent()

        val icons = parser.parse(metadata)

        assertThat(icons.map { it.name }).containsExactly("accessibility", "add-circle-outline")
        assertThat(icons[1].tags).isEqualTo(listOf("add", "circle", "outline", "plus"))
    }
}
