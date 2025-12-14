package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.CategoryHeader
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.GridItem
import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconItem

/**
 * Extension function to convert a map of categories to icons into a flat list of grid items.
 */
fun Map<Category, List<IconModel>>.toGridItems(): List<GridItem> = buildList {
    this@toGridItems
        .toSortedMap(compareBy { it.name })
        .forEach { (category, icons) ->
            add(CategoryHeader(category.name))
            icons.forEach { icon ->
                add(IconItem(icon, icon.originalName))
            }
        }
}
