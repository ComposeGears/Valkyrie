package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings

import androidx.compose.runtime.Stable

/**
 * Common interface for icon settings that can be applied during web import.
 * Implemented by both standard providers (SizeSettings) and Material (FontSettings).
 */
@Stable
interface IconSettings {
    val isModified: Boolean
}
