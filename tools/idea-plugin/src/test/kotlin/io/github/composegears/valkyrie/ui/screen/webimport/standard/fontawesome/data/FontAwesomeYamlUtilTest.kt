package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class FontAwesomeYamlUtilTest {

    @Test
    fun `parseYamlMap parses nested map structures`() {
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
        """.trimIndent()

        val root = parseYamlMap(yaml)
        val icon = root.getValue("address-book").asYamlMap()
        val search = icon.getValue("search").asYamlMap()

        assertThat(icon.getValue("label").asYamlString()).isEqualTo("Address Book")
        assertThat(search.getValue("terms").asYamlStringList()).containsExactly("address", "contact")
        assertThat(icon.getValue("styles").asYamlStringList()).containsExactly("solid", "regular")
    }

    @Test
    fun `parseYamlMap returns empty map for non-map root yaml`() {
        val yaml = """
            - solid
            - regular
        """.trimIndent()

        assertThat(parseYamlMap(yaml)).isEqualTo(emptyMap())
    }

    @Test
    fun `yaml helpers fall back for missing or unsupported values`() {
        assertThat((null).asYamlString()).isEqualTo("")
        assertThat((null).asYamlMap()).isEqualTo(emptyMap())
        assertThat((null).asYamlStringList()).containsExactly()
        assertThat(42.asYamlString()).isEqualTo("42")
        assertThat(42.asYamlMap()).isEqualTo(emptyMap())
        assertThat(listOf("solid", 42, true).asYamlStringList()).containsExactly("solid", "42", "true")
    }

    @Test
    fun `yaml map accessors normalize nested values`() {
        val icon = mapOf(
            "label" to " Address Book ",
            "search" to mapOf("terms" to listOf(" address ", "", "contact")),
            "styles" to listOf(" solid ", "regular"),
        )

        assertThat(icon.getYamlString("label")).isEqualTo("Address Book")
        assertThat(icon.getYamlMap("search").getYamlStringList("terms")).containsExactly("address", "contact")
        assertThat(icon.getYamlStringList("styles")).containsExactly("solid", "regular")
    }
}
