package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model

/**
 * Configuration model for standard icon providers (Lucide, Bootstrap, etc.).
 *
 * Icons are grouped by inferred categories and provide lookup by name.
 *
 * @property gridItems Map of categories to their icons (used for grid display)
 * @property categories List of all available categories (including "All")
 * @property styles List of available styles for providers that expose style variants
 * @property iconsByName Map of icon names to icon data (for quick lookup)
 */
data class StandardIconConfig(
    val gridItems: Map<InferredCategory, List<StandardIcon>>,
    val categories: List<InferredCategory>,
    val styles: List<IconStyle>,
    val iconsByName: Map<String, StandardIcon>,
)

/**
 * Extension function to build a [StandardIconConfig] from a list of [StandardIcon]s.
 * Groups icons by category and extracts distinct sorted categories/styles.
 */
fun List<StandardIcon>.toStandardIconConfig(): StandardIconConfig {
    val categories = this
        .map { it.category }
        .distinctBy { it.id }
        .sortedBy { it.name }

    val groupedIcons = this.groupBy { it.category }
    val styles = mapNotNull { it.style }
        .distinctBy { it.id }
        .sortedBy { it.name }
    val iconsByName = associateBy { it.name }

    return StandardIconConfig(
        gridItems = groupedIcons,
        categories = listOf(InferredCategory.All) + categories,
        styles = styles,
        iconsByName = iconsByName,
    )
}
