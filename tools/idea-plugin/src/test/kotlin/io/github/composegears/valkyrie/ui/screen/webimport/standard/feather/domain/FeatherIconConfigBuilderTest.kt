package io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import org.junit.jupiter.api.Test

class FeatherIconConfigBuilderTest {

    @Test
    fun `build icons from feather glyphmap codepoints`() {
        val icons = buildFeatherIcons(
            codepoints = mapOf(
                "activity" to 61696,
                "arrow-left" to 61712,
            ),
        )

        assertThat(icons).hasSize(2)
        assertThat(icons.map { it.name }).containsExactly("activity", "arrow-left")
        assertThat(icons.map { it.displayName }).containsExactly("Activity", "Arrow Left")
        assertThat(icons.map { it.codepoint.value }).containsExactly(61696, 61712)
        assertThat(icons.map { it.tags }).containsExactly(emptyList<String>(), emptyList<String>())
        assertThat(icons.map { it.style }).containsExactly(null, null)
        assertThat(icons[1].category).isEqualTo(InferredCategory(id = "arrows", name = "Arrows"))
    }
}
