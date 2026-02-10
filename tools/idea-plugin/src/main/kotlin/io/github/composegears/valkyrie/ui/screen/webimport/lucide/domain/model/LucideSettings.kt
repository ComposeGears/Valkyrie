package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model

data class LucideSettings(
    val size: Int = DEFAULT_SIZE,
) {
    companion object {
        const val DEFAULT_SIZE = 24
    }

    init {
        require(size in 16..48) { "Size must be between 16 and 48" }
    }

    val isModified: Boolean
        get() = size != DEFAULT_SIZE
}
