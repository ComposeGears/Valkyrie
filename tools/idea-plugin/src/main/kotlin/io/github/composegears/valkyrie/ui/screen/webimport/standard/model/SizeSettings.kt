package io.github.composegears.valkyrie.ui.screen.webimport.standard.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconSettings

/**
 * Size settings for standard icon providers.
 * Allows customization of icon dimensions (width/height).
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
