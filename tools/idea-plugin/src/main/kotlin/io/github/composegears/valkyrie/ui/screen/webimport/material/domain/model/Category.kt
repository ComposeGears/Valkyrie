package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.WebCategory

@JvmInline
value class Category(
    override val name: String,
) : WebCategory {
    companion object {
        val All = Category("All")
    }
}
