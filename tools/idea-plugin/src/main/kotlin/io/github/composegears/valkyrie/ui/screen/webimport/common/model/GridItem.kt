package io.github.composegears.valkyrie.ui.screen.webimport.common.model

/**
 * Base interface for grid items that can be displayed in icon grids
 */
sealed interface GridItem {
    val id: String
}

/**
 * A category header in the grid
 */
data class CategoryHeader(
    val categoryName: String,
) : GridItem {
    override val id: String = "header-$categoryName"
}

/**
 * An icon item in the grid
 */
data class IconItem<T>(
    val icon: T,
    val iconId: String,
) : GridItem {
    override val id: String = "icon-$iconId"
}
