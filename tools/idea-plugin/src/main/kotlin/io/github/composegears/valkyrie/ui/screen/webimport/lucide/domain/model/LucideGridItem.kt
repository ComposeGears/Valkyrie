package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconItem

/**
 * Extension function to convert a map of categories to icons into a flat list of grid items.
 */
fun Map<Category, List<LucideIcon>>.toGridItems(): List<GridItem> {
    return flatMap { (category, icons) ->
        listOf(CategoryHeader(category.title)) + icons.map { IconItem(it, it.name) }
    }
}
