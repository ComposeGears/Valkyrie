package io.github.composegears.valkyrie.ui.screen.webimport.common.model

/**
 * Common interface for icon settings that can be applied during web import.
 * Implemented by both standard providers (SizeSettings) and Material (FontSettings).
 */
interface IconSettings {
    val isModified: Boolean
}
