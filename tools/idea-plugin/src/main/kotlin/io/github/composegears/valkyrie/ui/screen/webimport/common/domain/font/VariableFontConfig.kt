package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight

/**
 * Configuration for variable fonts that support multiple axes (e.g. fill, grade, optical size).
 *
 * Used by providers like Material Symbols to control font rendering via variable font technology.
 * Standard providers (Lucide, Bootstrap, etc.) do not provide this configuration.
 *
 * @property variation Font variation settings (axes such as FILL, GRAD, opsz)
 * @property weights List of font weights the variable font supports
 * @property weight Currently selected weight to use for rendering
 * @property opticalSize Optional override for the icon display size (in dp/sp). When null, SizeSettings.size is used
 */
@Stable
data class VariableFontConfig(
    val variation: FontVariation.Settings,
    val weights: List<FontWeight>,
    val weight: FontWeight,
    val opticalSize: Float? = null,
)
