package io.github.composegears.valkyrie.sdk.ir.core

sealed interface IrFill {
    data class Color(val irColor: IrColor) : IrFill

    data class LinearGradient(
        val startY: Float,
        val startX: Float,
        val endY: Float,
        val endX: Float,
        val colorStops: MutableList<ColorStop> = mutableListOf(),
    ) : IrFill

    data class RadialGradient(
        val radius: Float,
        val centerX: Float,
        val centerY: Float,
        val colorStops: MutableList<ColorStop> = mutableListOf(),
    ) : IrFill

    data class ColorStop(
        val offset: Float,
        val irColor: IrColor,
    )
}
