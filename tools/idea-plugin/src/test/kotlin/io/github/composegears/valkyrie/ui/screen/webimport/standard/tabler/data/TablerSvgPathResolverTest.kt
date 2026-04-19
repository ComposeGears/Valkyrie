package io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import org.junit.jupiter.api.Test

class TablerSvgPathResolverTest {
    private val outlineStyle = IconStyle(id = "outline", name = "Outline")
    private val filledStyle = IconStyle(id = "filled", name = "Filled")

    @Test
    fun `resolve outline icon svg url`() {
        assertThat(resolveTablerSvgUrl("activity", outlineStyle)).isEqualTo(
            "https://cdn.jsdelivr.net/npm/@tabler/icons@latest/icons/outline/activity.svg",
        )
    }

    @Test
    fun `resolve filled icon svg url`() {
        assertThat(resolveTablerSvgUrl("accessible", filledStyle)).isEqualTo(
            "https://cdn.jsdelivr.net/npm/@tabler/icons@latest/icons/filled/accessible.svg",
        )
    }
}
