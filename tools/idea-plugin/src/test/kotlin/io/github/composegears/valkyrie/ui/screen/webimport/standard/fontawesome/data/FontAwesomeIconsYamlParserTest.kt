package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.jupiter.api.Test

class FontAwesomeIconsYamlParserTest {

    @Test
    fun `parses single style icon`() {
        val parser = FontAwesomeIconsYamlParser()
        val yaml = """
            '0':
              label: '0'
              search:
                terms:
                  - '0'
                  - digit zero
              styles:
                - solid
              unicode: '30'
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result).hasSize(1)
        assertThat(result.single()).all {
            prop(FontAwesomeIconMetadata::name).isEqualTo("0")
            prop(FontAwesomeIconMetadata::label).isEqualTo("0")
            prop(FontAwesomeIconMetadata::styles).containsExactly("solid")
            prop(FontAwesomeIconMetadata::unicodeHex).isEqualTo("30")
            prop(FontAwesomeIconMetadata::searchTerms).containsExactly("0", "digit zero")
        }
    }

    @Test
    fun `parses multi-style icon`() {
        val parser = FontAwesomeIconsYamlParser()
        val yaml = """
            address-book:
              label: Address Book
              search:
                terms:
                  - address
                  - contact
              styles:
                - solid
                - regular
              unicode: f2b9
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result).hasSize(1)
        assertThat(result.single()).all {
            prop(FontAwesomeIconMetadata::name).isEqualTo("address-book")
            prop(FontAwesomeIconMetadata::label).isEqualTo("Address Book")
            prop(FontAwesomeIconMetadata::styles).containsExactly("solid", "regular")
            prop(FontAwesomeIconMetadata::unicodeHex).isEqualTo("f2b9")
        }
    }

    @Test
    fun `parses brands icon`() {
        val parser = FontAwesomeIconsYamlParser()
        val yaml = """
            github:
              label: GitHub
              search:
                terms:
                  - brand
                  - code
              styles:
                - brands
              unicode: f09b
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result).hasSize(1)
        assertThat(result.single()).all {
            prop(FontAwesomeIconMetadata::name).isEqualTo("github")
            prop(FontAwesomeIconMetadata::label).isEqualTo("GitHub")
            prop(FontAwesomeIconMetadata::styles).containsExactly("brands")
            prop(FontAwesomeIconMetadata::unicodeHex).isEqualTo("f09b")
        }
    }

    @Test
    fun `skips icons with blank name or unicode`() {
        val parser = FontAwesomeIconsYamlParser()
        val yaml = """
            valid-icon:
              label: Valid
              styles:
                - solid
              unicode: f000
            '':
              label: Blank Name
              styles:
                - solid
              unicode: f001
            blank-unicode:
              label: Blank Unicode
              styles:
                - solid
              unicode: ''
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result).hasSize(1)
        assertThat(result.single().name).isEqualTo("valid-icon")
    }

    @Test
    fun `skips icons with invalid unicode hex`() {
        val parser = FontAwesomeIconsYamlParser()
        val yaml = """
            valid-icon:
              label: Valid
              styles:
                - solid
              unicode: f000
            invalid-icon:
              label: Invalid
              styles:
                - solid
              unicode: not-hex
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result).hasSize(1)
        assertThat(result.single().name).isEqualTo("valid-icon")
    }

    @Test
    fun `uses icon name as label fallback`() {
        val parser = FontAwesomeIconsYamlParser()
        val yaml = """
            my-icon:
              search:
                terms: []
              styles:
                - solid
              unicode: f000
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result.single().label).isEqualTo("my-icon")
    }

    @Test
    fun `cleans whitespace from string fields`() {
        val parser = FontAwesomeIconsYamlParser()
        val yaml = """
            ' icon-name ':
              label: ' Icon Label '
              search:
                terms:
                  - ' term1 '
                  - ''
                  - 'term2'
              styles:
                - ' solid '
                - ''
                - 'regular'
              unicode: ' f000 '
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result.single()).all {
            prop(FontAwesomeIconMetadata::name).isEqualTo("icon-name")
            prop(FontAwesomeIconMetadata::label).isEqualTo("Icon Label")
            prop(FontAwesomeIconMetadata::searchTerms).containsExactly("term1", "term2")
            prop(FontAwesomeIconMetadata::styles).containsExactly("solid", "regular")
            prop(FontAwesomeIconMetadata::unicodeHex).isEqualTo("f000")
        }
    }
}
