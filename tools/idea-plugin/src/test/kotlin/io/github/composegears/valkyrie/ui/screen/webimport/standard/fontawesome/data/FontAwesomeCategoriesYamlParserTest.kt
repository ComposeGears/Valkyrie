package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.InferredCategory
import org.junit.jupiter.api.Test

class FontAwesomeCategoriesYamlParserTest {

    @Test
    fun `parses category with icons`() {
        val parser = FontAwesomeCategoriesYamlParser()
        val yaml = """
            arrows:
              icons:
                - arrow-down
                - arrow-up
              label: Arrows
        """.trimIndent()

        val result = parser.parse(yaml)
        assertThat(result["arrow-down"]).isEqualTo(InferredCategory(id = "arrows", name = "Arrows"))
        assertThat(result["arrow-up"]).isEqualTo(InferredCategory(id = "arrows", name = "Arrows"))
    }

    @Test
    fun `parses multiple categories`() {
        val parser = FontAwesomeCategoriesYamlParser()
        val yaml = """
            communication:
              icons:
                - envelope
                - message
              label: Communication
            business:
              icons:
                - briefcase
              label: Business
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result["envelope"]).isEqualTo(InferredCategory(id = "communication", name = "Communication"))
        assertThat(result["message"]).isEqualTo(InferredCategory(id = "communication", name = "Communication"))
        assertThat(result["briefcase"]).isEqualTo(InferredCategory(id = "business", name = "Business"))
    }

    @Test
    fun `uses formatted category id as name fallback`() {
        val parser = FontAwesomeCategoriesYamlParser()
        val yaml = """
            no-label:
              icons:
                - fallback-item
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result["fallback-item"]).isEqualTo(InferredCategory(id = "no-label", name = "No Label"))
    }

    @Test
    fun `first category wins when icon appears in multiple`() {
        val parser = FontAwesomeCategoriesYamlParser()
        val yaml = """
            primary:
              icons:
                - shared-icon
              label: Primary
            secondary:
              icons:
                - shared-icon
              label: Secondary
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result["shared-icon"]).isEqualTo(InferredCategory(id = "primary", name = "Primary"))
    }

    @Test
    fun `skips empty category ids`() {
        val parser = FontAwesomeCategoriesYamlParser()
        val yaml = """
            '':
              icons:
                - ignored-icon
              label: Empty ID
            valid:
              icons:
                - valid-icon
              label: Valid
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result["ignored-icon"]).isEqualTo(null)
        assertThat(result["valid-icon"]).isEqualTo(InferredCategory(id = "valid", name = "Valid"))
    }

    @Test
    fun `cleans whitespace from icon names`() {
        val parser = FontAwesomeCategoriesYamlParser()
        val yaml = """
            test-category:
              icons:
                - ' icon-name '
                - ''
                - 'another-icon'
              label: Test
        """.trimIndent()

        val result = parser.parse(yaml)

        assertThat(result["icon-name"]).isEqualTo(InferredCategory(id = "test-category", name = "Test"))
        assertThat(result["another-icon"]).isEqualTo(InferredCategory(id = "test-category", name = "Test"))
    }
}
