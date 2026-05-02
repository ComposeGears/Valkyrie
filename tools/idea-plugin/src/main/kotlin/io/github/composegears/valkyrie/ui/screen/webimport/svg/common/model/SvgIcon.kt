package io.github.composegears.valkyrie.ui.screen.webimport.svg.common.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.StyledWebIcon

data class SvgIcon(
    override val name: String,
    val displayName: String,
    val exportName: String = name,
    val path: String,
    val tags: List<String>,
    override val category: InferredCategory,
    override val style: IconStyle? = null,
) : StyledWebIcon
