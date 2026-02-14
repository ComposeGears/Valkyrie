package io.github.composegears.valkyrie.ui.screen.webimport.standard.util

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.filterGridItems
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.StandardIconConfig

/**
 * Filters grid items from a [StandardIconConfig] with support for the "All" category.
 *
 * When the category is [InferredCategory.All], all icons are shown (no category filtering).
 * This is a convenience extension that handles the common "All" category pattern.
 *
 * @param category The selected category (All = show all)
 * @param searchQuery Optional search query for fuzzy matching
 * @return Filtered list of [GridItem]
 */
fun StandardIconConfig.filterByCategory(
    category: InferredCategory,
    searchQuery: String = "",
): List<GridItem> = gridItems.filterGridItems(
    category = category.takeUnless { it == InferredCategory.All },
    searchQuery = searchQuery,
)
