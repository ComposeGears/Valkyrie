package io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import org.junit.jupiter.api.Test

class TablerIconConfigBuilderTest {

    @Test
    fun `build icons from outline and filled codepoints`() {
        val icons = buildTablerIcons(
            outlineCodepoints = mapOf(
                "activity" to 0xF101,
                "accessible" to 0xF102,
            ),
            filledCodepoints = mapOf(
                "accessible" to 0xF6EA,
            ),
        )

        assertThat(icons).hasSize(3)
        assertThat(icons.map { it.name }).containsExactly("accessible", "accessible", "activity")
        assertThat(icons.map { it.style }).containsExactly(OUTLINE_STYLE, FILLED_STYLE, OUTLINE_STYLE)
        assertThat(icons.map { it.exportName }).containsExactly("accessible-outline", "accessible-filled", "activity")
        assertThat(icons.map { it.displayName }).containsExactly("Accessible", "Accessible", "Activity")
    }
}

private val OUTLINE_STYLE = IconStyle(id = "outline", name = "Outline")
private val FILLED_STYLE = IconStyle(id = "filled", name = "Filled")
