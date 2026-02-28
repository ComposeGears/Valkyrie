package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.util

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.filterGridItems
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.StandardIconConfig

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
    val categoryFiltered = when (category) {
        InferredCategory.All -> gridItems
        else -> gridItems.filterKeys { it.name == category.name }
    }

    val styleFiltered = when {
        style == null -> categoryFiltered
        else -> {
            categoryFiltered
                .mapValues { (_, icons) ->
                    icons.filter { icon -> icon.style?.id == style.id }
                }
                .filterValues { it.isNotEmpty() }
        }
    }

    return styleFiltered.filterGridItems(
        category = null,
        searchQuery = searchQuery,
    )
}
