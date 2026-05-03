package io.github.composegears.valkyrie.ui.screen.webimport.common.util

import io.github.composegears.valkyrie.sdk.utils.svg.SvgDomModifier
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.settings.SizeSettings

/**
 * Utility for applying size settings to SVG content.
 * Used by standard icon providers (Lucide, Bootstrap, etc.).
 */
object SvgSizeCustomizer {

    private const val ATTR_WIDTH = "width"
    private const val ATTR_HEIGHT = "height"

    fun applySettings(svgContent: String, settings: SizeSettings): String {
        return SvgDomModifier.modify(svgContent) { svgElement ->
            svgElement.setAttribute(ATTR_WIDTH, settings.size.toString())
            svgElement.setAttribute(ATTR_HEIGHT, settings.size.toString())
        }
    }
}
