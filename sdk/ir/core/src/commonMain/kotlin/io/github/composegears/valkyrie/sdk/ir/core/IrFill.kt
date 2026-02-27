package io.github.composegears.valkyrie.sdk.ir.core

sealed interface IrFill {
    data class Color(val irColor: IrColor) : IrFill

    sealed interface Gradient : IrFill {
        val colorStops: MutableList<ColorStop>
    }

    data class LinearGradient(
        val startY: Float,
        val startX: Float,
        val endY: Float,
        val endX: Float,
        override val colorStops: MutableList<ColorStop> = mutableListOf(),
    ) : Gradient

    data class RadialGradient(
        val radius: Float,
        val centerX: Float,
        val centerY: Float,
        override val colorStops: MutableList<ColorStop> = mutableListOf(),
    ) : Gradient

    data class ColorStop(
        val offset: Float,
        val irColor: IrColor,
    )
}
