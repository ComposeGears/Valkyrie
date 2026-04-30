package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data.IoniconsIconMetadata
import org.junit.jupiter.api.Test

class IoniconsIconConfigBuilderTest {

    @Test
    fun `build icons from metadata and glyphmap codepoints`() {
        val icons = buildIonicons(
            metadata = listOf(
                IoniconsIconMetadata(name = "accessibility", tags = listOf("accessibility")),
                IoniconsIconMetadata(name = "accessibility-outline", tags = listOf("accessibility", "outline")),
                IoniconsIconMetadata(name = "accessibility-sharp", tags = listOf("accessibility", "sharp")),
                IoniconsIconMetadata(name = "logo-github", tags = listOf("github")),
            ),
            codepoints = mapOf(
                "accessibility" to 61696,
                "accessibility-outline" to 61697,
                "accessibility-sharp" to 61698,
                "logo-github" to 62451,
            ),
        )

        assertThat(icons).hasSize(4)
        assertThat(icons.map { it.name }).containsExactly(
            "accessibility",
            "accessibility-outline",
            "accessibility-sharp",
            "logo-github",
        )
        assertThat(icons.map { it.displayName }).containsExactly(
            "Accessibility",
            "Accessibility",
            "Accessibility",
            "Logo Github",
        )
        assertThat(icons.map { it.exportName }).containsExactly(
            "accessibility-filled",
            "accessibility-outline",
            "accessibility-sharp",
            "logo-github",
        )
        assertThat(icons.map { it.style }).containsExactly(
            FILLED_STYLE,
            OUTLINE_STYLE,
            SHARP_STYLE,
            FILLED_STYLE,
        )
        assertThat(icons[0].category).isEqualTo(InferredCategory(id = "general", name = "General"))
    }
}

private val FILLED_STYLE = IconStyle(id = "filled", name = "Filled")
private val OUTLINE_STYLE = IconStyle(id = "outline", name = "Outline")
private val SHARP_STYLE = IconStyle(id = "sharp", name = "Sharp")
