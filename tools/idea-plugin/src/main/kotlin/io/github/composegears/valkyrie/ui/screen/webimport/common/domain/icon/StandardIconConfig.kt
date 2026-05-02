package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory

data class StandardIconConfig(
    override val gridItems: Map<InferredCategory, List<StandardIcon>>,
    override val categories: List<InferredCategory>,
    override val styles: List<IconStyle>,
) : WebIconConfig<StandardIcon>

/**
 * Extension function to build a [StandardIconConfig] from a list of [StandardIcon]s.
 * Groups icons by category and extracts distinct sorted categories/styles.
 */
fun List<StandardIcon>.toStandardIconConfig(): StandardIconConfig {
    val config = toWebIconConfig()
    return StandardIconConfig(
        gridItems = config.gridItems,
        categories = config.categories,
        styles = config.styles,
    )
}
