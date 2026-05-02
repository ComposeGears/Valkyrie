package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings

/**
 * Size settings for icon providers that customize exported SVG dimensions.
 */
data class SizeSettings(
    val size: Int = DEFAULT_SIZE,
) : IconSettings {
    companion object {
        const val DEFAULT_SIZE = 24
        const val MIN_SIZE = 16
        const val MAX_SIZE = 48
    }

    init {
        require(size in MIN_SIZE..MAX_SIZE) { "Size must be between $MIN_SIZE and $MAX_SIZE" }
    }

    override val isModified: Boolean
        get() = size != DEFAULT_SIZE
}
