package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model

import androidx.compose.ui.graphics.Color

data class LucideSettings(
    val color: Color = Color.Unspecified,
    val strokeWidth: Float = 2f,
    val size: Int = 24,
    val absoluteStrokeWidth: Boolean = false,
) {
    init {
        require(strokeWidth in 0.5f..4.0f) { "Stroke width must be between 0.5 and 4.0" }
        require(size in 16..96) { "Size must be between 16 and 96" }
    }

    val isModified: Boolean
        get() = color != Color.Unspecified || strokeWidth != 2f || size != 24 || absoluteStrokeWidth

    fun adjustedStrokeWidth(): Float {
        return if (absoluteStrokeWidth) {
            (strokeWidth * 24f) / size.toFloat()
        } else {
            strokeWidth
        }
    }
}
