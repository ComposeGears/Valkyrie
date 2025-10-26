package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model

sealed interface MaterialGridItem {

    val id: String

    data class CategoryHeader(
        val category: Category,
        override val id: String = category.name,
    ) : MaterialGridItem

    data class IconItem(
        val icon: IconModel,
        override val id: String = icon.originalName,
    ) : MaterialGridItem
}

fun Map<Category, List<IconModel>>.toGridItems(): List<MaterialGridItem> = buildList {
    this@toGridItems
        .toSortedMap(compareBy { it.name })
        .forEach { (category, icons) ->
            add(MaterialGridItem.CategoryHeader(category))
            icons.forEach { icon ->
                add(MaterialGridItem.IconItem(icon))
            }
        }
}
