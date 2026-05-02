package io.github.composegears.valkyrie.ui.screen.webimport.common.util

import com.github.androidpasswordstore.sublimefuzzy.Fuzzy
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.WebCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StyledWebIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.WebIcon
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.WebIconConfig

/**
 * Converts a map of categories to icons into a flat list of grid items.
 *
 * @return A list of [GridItem] containing [CategoryHeader] and [IconItem] elements,
 *         sorted by category and preserving icon order within each category
 */
fun <Category : WebCategory, Icon : WebIcon> Map<Category, List<Icon>>.toGridItems(): List<GridItem> = this
    .toSortedMap(compareBy { it.name })
    .flatMap { (category, icons) ->
        buildList {
            add(CategoryHeader(category.name))
            icons.onEach {
                add(IconItem(it, it.name))
            }
        }
    }

/**
 * Filters and searches through a grid of icons using fuzzy matching.
 *
 * @param category The category to filter by, or null to show all
 * @param searchQuery The search query to filter by (uses fuzzy matching)
 * @return A list of [GridItem] containing either [CategoryHeader] or [IconItem] elements
 */
fun <Category : WebCategory, Icon : WebIcon> Map<Category, List<Icon>>.filterGridItems(
    category: Category?,
    searchQuery: String = "",
): List<GridItem> {
    val categoryFiltered = when (category) {
        null -> this
        else -> filterKeys { it.name == category.name }
    }

    return if (searchQuery.isBlank()) {
        categoryFiltered.toGridItems()
    } else {
        categoryFiltered
            .asSequence()
            .flatMap { it.value }
            .map { it to Fuzzy.fuzzyMatch(searchQuery, it.name) }
            .filter { it.second.first }
            .sortedByDescending { it.second.second }
            .map { IconItem(it.first, it.first.name) }
            .toList()
    }
}

fun <Icon : StyledWebIcon> WebIconConfig<Icon>.filterByCategory(
    category: InferredCategory,
    style: IconStyle? = null,
    searchQuery: String = "",
): List<GridItem> {
    val categoryFiltered = when {
        category.id == InferredCategory.All.id -> gridItems
        else -> gridItems.filterKeys { it.id == category.id }
    }

    val styleId = style?.id
    val styleFiltered = when {
        styleId == null -> categoryFiltered
        else ->
            categoryFiltered
                .mapValues { (_, icons) -> icons.filter { it.style?.id == null || it.style?.id == styleId } }
                .filterValues { it.isNotEmpty() }
    }

    return styleFiltered.filterGridItems(
        category = null,
        searchQuery = searchQuery,
    )
}
