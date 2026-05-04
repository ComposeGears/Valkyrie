package io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data.CssGgIconMetadata
import org.junit.jupiter.api.Test

class CssGgConfigBuilderTest {

    @Test
    fun `build css gg icons`() {
        val icons = buildCssGgIcons(
            metadata = listOf(
                CssGgIconMetadata(
                    name = "add",
                    path = "/icons/svg/add.svg",
                    tags = listOf("add"),
                ),
                CssGgIconMetadata(
                    name = "arrow-bottom-left",
                    path = "/icons/svg/arrow-bottom-left.svg",
                    tags = listOf("arrow", "bottom", "left"),
                ),
            ),
        )

        assertThat(icons).hasSize(2)
        assertThat(icons.map { it.name }).containsExactly("add", "arrow-bottom-left")
        assertThat(icons.map { it.displayName }).containsExactly("Add", "Arrow Bottom Left")
        assertThat(icons.map { it.path }).containsExactly(
            "/icons/svg/add.svg",
            "/icons/svg/arrow-bottom-left.svg",
        )
        assertThat(icons.map { it.exportName }).containsExactly("add", "arrow-bottom-left")
        assertThat(icons[1].category).isEqualTo(InferredCategory(id = "arrows", name = "Arrows"))
    }
}
