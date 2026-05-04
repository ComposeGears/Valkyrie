package io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data.OcticonsIconMetadata
import org.junit.jupiter.api.Test

class OcticonsConfigBuilderTest {

    @Test
    fun `build octicons with size styles`() {
        val icons = buildOcticons(
            metadata = listOf(
                OcticonsIconMetadata(
                    name = "alert",
                    size = "16",
                    width = 16,
                    path = "alert-16.svg",
                    tags = listOf("alert", "warning"),
                ),
                OcticonsIconMetadata(
                    name = "alert",
                    size = "24",
                    width = 24,
                    path = "alert-24.svg",
                    tags = listOf("alert", "warning"),
                ),
                OcticonsIconMetadata(
                    name = "alert",
                    size = "12",
                    width = 12,
                    path = "alert-12.svg",
                    tags = listOf("alert", "warning"),
                ),
                OcticonsIconMetadata(
                    name = "feed-issue-reopen",
                    size = "16",
                    width = 17,
                    path = "feed-issue-reopen-16.svg",
                    tags = listOf("feed", "issue", "reopen"),
                ),
                OcticonsIconMetadata(
                    name = "lockup-github",
                    size = "16",
                    width = 68,
                    path = "lockup-github-16.svg",
                    tags = listOf("lockup", "github"),
                ),
                OcticonsIconMetadata(
                    name = "archive",
                    size = "16",
                    width = 16,
                    path = "archive-16.svg",
                    tags = listOf("archive"),
                ),
            ),
        )

        assertThat(icons).hasSize(4)
        assertThat(icons.map { it.name }).containsExactly("alert", "alert", "feed-issue-reopen", "archive")
        assertThat(icons.map { it.displayName }).containsExactly("Alert", "Alert", "Feed Issue Reopen", "Archive")
        assertThat(icons.map { it.path }).containsExactly(
            "alert-16.svg",
            "alert-24.svg",
            "feed-issue-reopen-16.svg",
            "archive-16.svg",
        )
        assertThat(icons.map { it.exportName }).containsExactly("alert-16", "alert-24", "feed-issue-reopen", "archive")
        assertThat(icons.map { it.style }).containsExactly(SIZE_16_STYLE, SIZE_24_STYLE, SIZE_16_STYLE, SIZE_16_STYLE)
        assertThat(icons[0].category).isEqualTo(InferredCategory(id = "general", name = "General"))
    }
}

private val SIZE_16_STYLE = IconStyle(id = "16", name = "16px")
private val SIZE_24_STYLE = IconStyle(id = "24", name = "24px")
