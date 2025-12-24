package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model

import androidx.compose.ui.graphics.Color

data class LucideSettings(
    val color: Color = Color.Unspecified,
    val strokeWidth: Float = DEFAULT_STROKE_WIDTH,
    val size: Int = DEFAULT_SIZE,
    val absoluteStrokeWidth: Boolean = false,
) {
    companion object {
        const val DEFAULT_STROKE_WIDTH = 2f
        const val DEFAULT_SIZE = 24
    }

    init {
        require(strokeWidth in 0.5f..4.0f) { "Stroke width must be between 0.5 and 4.0" }
        require(size in 16..48) { "Size must be between 16 and 48" }
    }

    val isModified: Boolean
        get() = color != Color.Unspecified ||
            strokeWidth != DEFAULT_STROKE_WIDTH ||
            size != DEFAULT_SIZE ||
            absoluteStrokeWidth

    fun adjustedStrokeWidth(): Float {
        return if (absoluteStrokeWidth) {
            (strokeWidth * DEFAULT_SIZE.toFloat()) / size.toFloat()
        } else {
            strokeWidth
        }
    }
}
