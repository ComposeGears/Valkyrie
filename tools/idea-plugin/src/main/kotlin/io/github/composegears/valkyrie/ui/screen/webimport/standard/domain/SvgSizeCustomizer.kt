package io.github.composegears.valkyrie.ui.screen.webimport.standard.domain

import io.github.composegears.valkyrie.parser.jvm.svg.SvgManipulator
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.SizeSettings

/**
 * Utility for applying size settings to SVG content.
 * Used by standard icon providers (Lucide, Bootstrap, etc.).
 */
object SvgSizeCustomizer {

    private const val ATTR_WIDTH = "width"
    private const val ATTR_HEIGHT = "height"

    fun applySettings(svgContent: String, settings: SizeSettings): String {
        return SvgManipulator.modifySvg(svgContent) { svgElement ->
            svgElement.setAttribute(ATTR_WIDTH, settings.size.toString())
            svgElement.setAttribute(ATTR_HEIGHT, settings.size.toString())
        }
    }
}
