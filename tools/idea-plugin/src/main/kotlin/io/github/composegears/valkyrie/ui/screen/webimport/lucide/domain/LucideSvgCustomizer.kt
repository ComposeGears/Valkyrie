package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain

import io.github.composegears.valkyrie.parser.jvm.svg.SvgManipulator
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings

object LucideSvgCustomizer {

    private const val ATTR_WIDTH = "width"
    private const val ATTR_HEIGHT = "height"

    fun applySettings(svgContent: String, settings: LucideSettings): String {
        return SvgManipulator.modifySvg(svgContent) { svgElement ->
            svgElement.setAttribute(ATTR_WIDTH, settings.size.toString())
            svgElement.setAttribute(ATTR_HEIGHT, settings.size.toString())
        }
    }
}
