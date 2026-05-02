package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data.HeroiconsIconMetadata
import org.junit.jupiter.api.Test

class HeroiconsConfigBuilderTest {

    @Test
    fun `build 24px outline and solid icons with simplified style names`() {
        val icons = buildHeroicons(
            metadata = listOf(
                HeroiconsIconMetadata(
                    name = "academic-cap",
                    styleId = "24-outline",
                    path = "/24/outline/academic-cap.svg",
                    tags = listOf("academic", "cap"),
                ),
                HeroiconsIconMetadata(
                    name = "academic-cap",
                    styleId = "24-solid",
                    path = "/24/solid/academic-cap.svg",
                    tags = listOf("academic", "cap"),
                ),
                HeroiconsIconMetadata(
                    name = "academic-cap",
                    styleId = "20-solid",
                    path = "/20/solid/academic-cap.svg",
                    tags = listOf("academic", "cap"),
                ),
                HeroiconsIconMetadata(
                    name = "academic-cap",
                    styleId = "16-solid",
                    path = "/16/solid/academic-cap.svg",
                    tags = listOf("academic", "cap"),
                ),
                HeroiconsIconMetadata(
                    name = "archive-box",
                    styleId = "24-outline",
                    path = "/24/outline/archive-box.svg",
                    tags = listOf("archive", "box"),
                ),
            ),
        )

        assertThat(icons).hasSize(3)
        assertThat(icons.map { it.name }).containsExactly("academic-cap", "academic-cap", "archive-box")
        assertThat(icons.map { it.displayName }).containsExactly("Academic Cap", "Academic Cap", "Archive Box")
        assertThat(icons.map { it.path }).containsExactly(
            "/24/outline/academic-cap.svg",
            "/24/solid/academic-cap.svg",
            "/24/outline/archive-box.svg",
        )
        assertThat(icons.map { it.exportName }).containsExactly(
            "academic-cap-outline",
            "academic-cap-solid",
            "archive-box",
        )
        assertThat(icons.map { it.style }).containsExactly(OUTLINE_STYLE, SOLID_STYLE, OUTLINE_STYLE)
        assertThat(icons[0].category).isEqualTo(InferredCategory(id = "general", name = "General"))
    }
}

private val OUTLINE_STYLE = IconStyle(id = "outline", name = "Outline")
private val SOLID_STYLE = IconStyle(id = "solid", name = "Solid")
