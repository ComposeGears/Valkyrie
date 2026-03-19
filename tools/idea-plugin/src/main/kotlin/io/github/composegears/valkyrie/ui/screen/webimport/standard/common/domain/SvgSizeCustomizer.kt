package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain

import io.github.composegears.valkyrie.sdk.utils.svg.SvgDomModifier
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SizeSettings
import org.w3c.dom.Element

/**
 * Utility for applying size settings to SVG content.
 * Used by standard icon providers (Lucide, Bootstrap, etc.).
 */
object SvgSizeCustomizer {

    private const val ATTR_WIDTH = "width"
    private const val ATTR_HEIGHT = "height"
    private const val ATTR_FILL = "fill"
    private const val ATTR_STROKE = "stroke"
    private const val ATTR_COLOR = "color"
    private const val ATTR_TRANSFORM = "transform"
    private const val ATTR_VIEW_BOX = "viewBox"
    private const val CURRENT_COLOR = "currentColor"

    fun applySettings(svgContent: String, settings: SizeSettings): String {
        return SvgDomModifier.modify(svgContent) { svgElement ->
            svgElement.setAttribute(ATTR_WIDTH, settings.size.toString())
            svgElement.setAttribute(ATTR_HEIGHT, settings.size.toString())

            settings.color?.let { color ->
                svgElement.setAttribute(ATTR_COLOR, color)
                if (!svgElement.hasAttribute(ATTR_FILL)) {
                    svgElement.setAttribute(ATTR_FILL, color)
                }
                SvgDomModifier.updateAttributeConditionally(svgElement, ATTR_FILL, CURRENT_COLOR, color)
                SvgDomModifier.updateAttributeConditionally(svgElement, ATTR_STROKE, CURRENT_COLOR, color)
            }

            buildTransform(svgElement, settings)?.let { transform ->
                svgElement.setAttribute(ATTR_TRANSFORM, transform)
            }
        }
    }

    private fun buildTransform(
        svgElement: Element,
        settings: SizeSettings,
    ): String? {
        if (
            settings.rotation == SizeSettings.DEFAULT_ROTATION &&
            !settings.flipHorizontally &&
            !settings.flipVertically
        ) {
            return null
        }

        val bounds = parseBounds(svgElement)
        val transforms = buildList {
            if (settings.rotation != SizeSettings.DEFAULT_ROTATION) {
                add("rotate(${settings.rotation} ${bounds.centerX} ${bounds.centerY})")
            }
            if (settings.flipHorizontally) {
                add("translate(${bounds.flipX} 0) scale(-1 1)")
            }
            if (settings.flipVertically) {
                add("translate(0 ${bounds.flipY}) scale(1 -1)")
            }
        }

        return transforms.joinToString(" ")
    }

    private fun parseBounds(svgElement: Element): SvgBounds {
        val viewBox = svgElement.getAttribute(ATTR_VIEW_BOX)
            .split(',', ' ')
            .filter { it.isNotBlank() }
            .mapNotNull { it.toDoubleOrNull() }

        if (viewBox.size == 4) {
            return SvgBounds(
                centerX = (viewBox[0] + viewBox[2] / 2).toString(),
                centerY = (viewBox[1] + viewBox[3] / 2).toString(),
                flipX = (viewBox[0] * 2 + viewBox[2]).toString(),
                flipY = (viewBox[1] * 2 + viewBox[3]).toString(),
            )
        }

        val width = svgElement.getAttribute(ATTR_WIDTH).toDoubleOrNull() ?: SizeSettings.DEFAULT_SIZE.toDouble()
        val height = svgElement.getAttribute(ATTR_HEIGHT).toDoubleOrNull() ?: SizeSettings.DEFAULT_SIZE.toDouble()
        return SvgBounds(
            centerX = (width / 2).toString(),
            centerY = (height / 2).toString(),
            flipX = width.toString(),
            flipY = height.toString(),
        )
    }

    private data class SvgBounds(
        val centerX: String,
        val centerY: String,
        val flipX: String,
        val flipY: String,
    )
}
