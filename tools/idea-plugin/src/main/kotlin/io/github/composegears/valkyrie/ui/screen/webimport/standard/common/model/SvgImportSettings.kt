package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model

import io.github.composegears.valkyrie.ui.screen.webimport.common.model.IconSettings

/**
 * Shared SVG customization settings for standard icon providers.
 *
 * The current UI only exposes size, but the shared model also carries
 * transformer-backed color and transform options used by the standard web import flow.
 */
data class SvgImportSettings(
    val size: Int = DEFAULT_SIZE,
    val color: String? = null,
    val rotation: Int = DEFAULT_ROTATION,
    val flipHorizontally: Boolean = false,
    val flipVertically: Boolean = false,
) : IconSettings {
    companion object {
        const val DEFAULT_SIZE = 24
        const val MIN_SIZE = 16
        const val MAX_SIZE = 48
        const val DEFAULT_ROTATION = 0
    }

    init {
        require(size in MIN_SIZE..MAX_SIZE) { "Size must be between $MIN_SIZE and $MAX_SIZE" }
    }

    override val isModified: Boolean
        get() =
            size != DEFAULT_SIZE ||
                color != null ||
                rotation != DEFAULT_ROTATION ||
                flipHorizontally ||
                flipVertically
}
