package io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class SimpleIconsMetadataParserTest {

    @Test
    fun `parse simple icons font metadata`() {
        val parser = SimpleIconsMetadataParser()
        val metadataJson = """
            [
              {
                "title": ".ENV",
                "slug": "dotenv",
                "code": "ea01",
                "hex": "ECD53F",
                "source": "https://github.com/motdotla/dotenv",
                "aliases": {
                  "aka": ["Dotenv"],
                  "dup": [{ "title": "Dot Env" }],
                  "loc": { "ja": "ドットエンブ" }
                }
              },
              {
                "title": ".NET",
                "slug": "dotnet",
                "code": "ea02",
                "hex": "512BD4",
                "source": "https://github.com/dotnet/brand"
              },
              {
                "title": "Broken",
                "slug": "broken",
                "hex": "000000"
              }
            ]
        """.trimIndent()

        val icons = parser.parse(metadataJson)

        assertThat(icons.map { it.title }).containsExactly(".ENV", ".NET")
        assertThat(icons.map { it.slug }).containsExactly("dotenv", "dotnet")
        assertThat(icons.map { it.codepoint }).containsExactly(0xea01, 0xea02)
        assertThat(icons.map { it.hex }).containsExactly("ECD53F", "512BD4")
        assertThat(icons[0].aliases).isEqualTo(listOf("Dotenv", "Dot Env", "ドットエンブ"))
        assertThat(icons[1].aliases).isEqualTo(emptyList())
    }
}
