package io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model

@JvmInline
value class Category(val name: String) {

    companion object {
        val All = Category("All")
    }
}
