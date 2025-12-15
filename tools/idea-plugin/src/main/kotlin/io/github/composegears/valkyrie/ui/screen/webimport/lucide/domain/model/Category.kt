package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model

data class Category(
    val id: String,
    val title: String,
) {
    companion object {
        val All = Category(id = "all", title = "All")
    }
}
