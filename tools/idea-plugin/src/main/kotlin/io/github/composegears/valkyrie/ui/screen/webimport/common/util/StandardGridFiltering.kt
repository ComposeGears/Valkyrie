package io.github.composegears.valkyrie.ui.screen.webimport.common.util

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StandardIconConfig

/**
 * Filters grid items from a [StandardIconConfig] with support for category, style, and search.
 *
 * When the category is [InferredCategory.All], all categories are included.
 * When [style] is `null`, no style filtering is applied.
 *
 * @param category The selected category (All = show all)
 * @param style The selected style, or `null` to include all styles
 * @param searchQuery Optional search query for fuzzy matching
 * @return Filtered list of [GridItem]
 */
fun StandardIconConfig.filterByCategory(
    category: InferredCategory,
    style: IconStyle? = null,
    searchQuery: String = "",
): List<GridItem> {
    val categoryFiltered = when {
        category.id == InferredCategory.All.id -> gridItems
        else -> gridItems.filterKeys { it.id == category.id }
    }

    val styleFiltered = when {
        style == null -> categoryFiltered
        else -> {
            categoryFiltered
                .mapValues { (_, icons) ->
                    // Icons with style == null are treated as "belongs to all styles"
                    // (used by providers like Material Symbols where style = font family, not per-icon variant).
                    icons.filter { icon -> icon.style == null || icon.style.id == style.id }
                }
                .filterValues { it.isNotEmpty() }
        }
    }

    return styleFiltered.filterGridItems(
        category = null,
        searchQuery = searchQuery,
    )
}
