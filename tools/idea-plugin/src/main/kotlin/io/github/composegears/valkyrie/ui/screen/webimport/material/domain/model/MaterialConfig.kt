package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model

data class MaterialConfig(
    val gridItems: Map<Category, List<IconModel>>,
    val categories: List<Category>,
)

data class IconModel(
    val name: String,
    val originalName: String,
    val codepoint: Int,
    val category: Category,
)
