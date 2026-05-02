package io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.WebIconConfig
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.toWebIconConfig

data class SvgIconConfig(
    override val gridItems: Map<InferredCategory, List<SvgIcon>>,
    override val categories: List<InferredCategory>,
    override val styles: List<IconStyle>,
) : WebIconConfig<SvgIcon>

fun List<SvgIcon>.toSvgIconConfig(): SvgIconConfig {
    val config = toWebIconConfig()
    return SvgIconConfig(
        gridItems = config.gridItems,
        categories = config.categories,
        styles = config.styles,
    )
}
