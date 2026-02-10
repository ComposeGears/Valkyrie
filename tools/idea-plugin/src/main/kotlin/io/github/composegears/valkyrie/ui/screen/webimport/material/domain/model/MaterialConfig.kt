package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.WebIcon

data class MaterialConfig(
    val gridItems: Map<Category, List<IconModel>>,
    val categories: List<Category>,
)

data class IconModel(
    override val name: String,
    val originalName: String,
    val codepoint: Int,
    val category: Category,
) : WebIcon
