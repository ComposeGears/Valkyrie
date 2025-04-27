package io.github.composegears.valkyrie.ir

data class IrImageVector(
    val name: String = "",
    val autoMirror: Boolean = false,
    val defaultWidth: Float,
    val defaultHeight: Float,
    val viewportWidth: Float,
    val viewportHeight: Float,
    val nodes: List<IrVectorNode>,
)

sealed interface IrVectorNode {
    data class IrGroup(
        val name: String = "",
        val rotate: Float = 0f,
        val pivotX: Float = 0f,
        val pivotY: Float = 0f,
        val scaleX: Float = 1f,
        val scaleY: Float = 1f,
        val translationX: Float = 0f,
        val translationY: Float = 0f,
        val clipPathData: MutableList<IrPathNode>,
        val paths: MutableList<IrPath>,
    ) : IrVectorNode

    data class IrPath(
        val name: String = "",
        val fill: IrFill? = null,
        val fillAlpha: Float = 1f,
        val stroke: IrStroke? = null,
        val strokeAlpha: Float = 1f,
        val strokeLineWidth: Float = 0f,
        val strokeLineCap: IrStrokeLineCap = IrStrokeLineCap.Butt,
        val strokeLineJoin: IrStrokeLineJoin = IrStrokeLineJoin.Miter,
        val strokeLineMiter: Float = 4f,
        val pathFillType: IrPathFillType = IrPathFillType.NonZero,
        val paths: List<IrPathNode>,
    ) : IrVectorNode
}

enum class IrPathFillType {
    EvenOdd,
    NonZero,
}

enum class IrStrokeLineCap {
    Butt,
    Round,
    Square,
}

enum class IrStrokeLineJoin {
    Miter,
    Round,
    Bevel,
}

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

sealed interface IrStroke {
    data class Color(val irColor: IrColor) : IrStroke
}

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
