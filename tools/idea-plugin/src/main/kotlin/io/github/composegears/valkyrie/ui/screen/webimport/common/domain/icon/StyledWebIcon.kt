package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory

interface StyledWebIcon : WebIcon {
    val category: InferredCategory
    val style: IconStyle?
}
