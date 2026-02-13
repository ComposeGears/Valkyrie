package io.github.composegears.valkyrie.ui.screen.webimport.standard.model

/**
 * Configuration model for standard icon providers (Lucide, Bootstrap, etc.).
 *
 * Icons are grouped by inferred categories and provide lookup by name.
 *
 * @property gridItems Map of categories to their icons (used for grid display)
 * @property categories List of all available categories (including "All")
 * @property iconsByName Map of icon names to icon data (for quick lookup)
 */
data class StandardIconConfig(
    val gridItems: Map<InferredCategory, List<StandardIcon>>,
    val categories: List<InferredCategory>,
    val iconsByName: Map<String, StandardIcon>,
)

/**
 * Extension function to build a [StandardIconConfig] from a list of [StandardIcon]s.
 * Groups icons by category and extracts distinct sorted categories.
 */
fun List<StandardIcon>.toStandardIconConfig(): StandardIconConfig {
    val categories = this
        .map { it.category }
        .distinctBy { it.id }
        .sortedBy { it.name }

    val groupedIcons = this.groupBy { it.category }
    val iconsByName = associateBy { it.name }

    return StandardIconConfig(
        gridItems = groupedIcons,
        categories = listOf(InferredCategory.All) + categories,
        iconsByName = iconsByName,
    )
}
