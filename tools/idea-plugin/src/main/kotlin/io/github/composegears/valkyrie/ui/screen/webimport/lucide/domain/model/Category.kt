package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.WebCategory

data class Category(
    val id: String,
    override val name: String,
) : WebCategory {
    companion object {
        val All = Category(id = "all", name = "All")
    }
}
