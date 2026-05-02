package io.github.composegears.valkyrie.ui.screen.webimport.svg.common.data

object SvgPreviewColorNormalizer {

    fun normalize(svg: String): String {
        val inheritsFill = ROOT_CURRENT_COLOR_FILL.containsMatchIn(svg)
        val inheritsStroke = ROOT_CURRENT_COLOR_STROKE.containsMatchIn(svg)

        var normalized = CURRENT_COLOR_PAINT_ATTRIBUTE.replace(svg) { match ->
            "${match.groupValues[1]}$MASK_COLOR${match.groupValues[2]}"
        }
        if (inheritsFill) {
            normalized = normalized.addMissingPathPaint(attribute = "fill")
        }
        if (inheritsStroke) {
            normalized = normalized.addMissingPathPaint(attribute = "stroke")
        }
        return normalized
    }

    private fun String.addMissingPathPaint(attribute: String): String {
        val pathWithoutPaint = Regex("""<path\b(?![^>]*\b$attribute\s*=)""")
        return pathWithoutPaint.replace(this, "<path $attribute=\"$MASK_COLOR\"")
    }

    private const val MASK_COLOR = "#000000"
    private val CURRENT_COLOR_PAINT_ATTRIBUTE = Regex("""\b((?:fill|stroke)\s*=\s*["'])currentColor(["'])""")
    private val ROOT_CURRENT_COLOR_FILL = Regex("""<svg\b[^>]*\bfill\s*=\s*["']currentColor["']""")
    private val ROOT_CURRENT_COLOR_STROKE = Regex("""<svg\b[^>]*\bstroke\s*=\s*["']currentColor["']""")
}
