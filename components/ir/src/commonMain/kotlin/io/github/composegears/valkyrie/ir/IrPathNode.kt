package io.github.composegears.valkyrie.ir

sealed interface IrPathNode {

    data object Close : IrPathNode
    data class RelativeMoveTo(
        val x: Float,
        val y: Float,
    ) : IrPathNode

    data class MoveTo(
        val x: Float,
        val y: Float,
    ) : IrPathNode

    data class RelativeLineTo(
        val x: Float,
        val y: Float,
    ) : IrPathNode

    data class LineTo(
        val x: Float,
        val y: Float,
    ) : IrPathNode

    data class RelativeHorizontalTo(val x: Float) : IrPathNode
    data class HorizontalTo(val x: Float) : IrPathNode
    data class RelativeVerticalTo(val y: Float) : IrPathNode
    data class VerticalTo(val y: Float) : IrPathNode
    data class RelativeCurveTo(
        val dx1: Float,
        val dy1: Float,
        val dx2: Float,
        val dy2: Float,
        val dx3: Float,
        val dy3: Float,
    ) : IrPathNode

    data class CurveTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
        val x3: Float,
        val y3: Float,
    ) : IrPathNode

    data class RelativeReflectiveCurveTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
    ) : IrPathNode

    data class ReflectiveCurveTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
    ) : IrPathNode

    data class RelativeQuadTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
    ) : IrPathNode

    data class QuadTo(
        val x1: Float,
        val y1: Float,
        val x2: Float,
        val y2: Float,
    ) : IrPathNode

    data class RelativeReflectiveQuadTo(
        val x: Float,
        val y: Float,
    ) : IrPathNode

    data class ReflectiveQuadTo(
        val x: Float,
        val y: Float,
    ) : IrPathNode

    data class RelativeArcTo(
        val horizontalEllipseRadius: Float,
        val verticalEllipseRadius: Float,
        val theta: Float,
        val isMoreThanHalf: Boolean,
        val isPositiveArc: Boolean,
        val arcStartDx: Float,
        val arcStartDy: Float,
    ) : IrPathNode

    data class ArcTo(
        val horizontalEllipseRadius: Float,
        val verticalEllipseRadius: Float,
        val theta: Float,
        val isMoreThanHalf: Boolean,
        val isPositiveArc: Boolean,
        val arcStartX: Float,
        val arcStartY: Float,
    ) : IrPathNode
}
