package io.github.composegears.valkyrie.ui.screen.webimport.common.util

import com.github.androidpasswordstore.sublimefuzzy.Fuzzy
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconItem

/**
 * Converts a map of categories to icons into a flat list of grid items.
 *
 * @param sortKey Function to extract the sortable key from a category
 * @param idExtractor Function to extract the ID string from an icon item
 * @return A list of [GridItem] containing [CategoryHeader] and [IconItem] elements,
 *         sorted by category and preserving icon order within each category
 */
fun <C, T> Map<C, List<T>>.toGridItems(
    sortKey: C.() -> String,
    idExtractor: T.() -> String,
): List<GridItem> {
    return toSortedMap(compareBy { it.sortKey() })
        .flatMap { (category, icons) ->
            listOf(CategoryHeader(category.sortKey())) + icons.map { IconItem(it, it.idExtractor()) }
        }
}

/**
 * Filters and searches through a grid of icons using fuzzy matching.
 *
 * @param category The category to filter by, or null to show all
 * @param searchQuery The search query to filter by (uses fuzzy matching)
 * @param sortKey Function to extract the sortable key from a category (for category filtering only)
 * @param idExtractor Function to extract the ID string from an icon item
 * @param categoryMatcher Function to check if a category matches the selected one
 * @return A list of [GridItem] containing either [CategoryHeader] or [IconItem] elements
 */
fun <C, T> Map<C, List<T>>.filterGridItems(
    category: C?,
    searchQuery: String = "",
    sortKey: C.() -> String = { toString() },
    idExtractor: T.() -> String,
    categoryMatcher: (C) -> Boolean = { it == category },
): List<GridItem> {
    val categoryFiltered = if (category == null) {
        this
    } else {
        filterKeys(categoryMatcher)
    }

    return if (searchQuery.isBlank()) {
        categoryFiltered.toGridItems(sortKey, idExtractor)
    } else {
        categoryFiltered
            .asSequence()
            .flatMap { it.value }
            .map { it to Fuzzy.fuzzyMatch(searchQuery, it.idExtractor()) }
            .filter { it.second.first }
            .sortedByDescending { it.second.second }
            .map { IconItem(it.first, it.first.idExtractor()) }
            .toList()
    }
}
