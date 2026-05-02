package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory

interface WebIconConfig<out Icon : StyledWebIcon> {
    val gridItems: Map<InferredCategory, List<Icon>>
    val categories: List<InferredCategory>
    val styles: List<IconStyle>
}

private data class DefaultWebIconConfig<Icon : StyledWebIcon>(
    override val gridItems: Map<InferredCategory, List<Icon>>,
    override val categories: List<InferredCategory>,
    override val styles: List<IconStyle>,
) : WebIconConfig<Icon>

fun <Icon : StyledWebIcon> List<Icon>.toWebIconConfig(): WebIconConfig<Icon> {
    val categories = map { it.category }
        .distinctBy { it.id }
        .sortedBy { it.name }
    val styles = mapNotNull { it.style }
        .distinctBy { it.id }
        .sortedBy { it.name }

    return DefaultWebIconConfig(
        gridItems = groupBy { it.category },
        categories = listOf(InferredCategory.All) + categories,
        styles = styles,
    )
}
