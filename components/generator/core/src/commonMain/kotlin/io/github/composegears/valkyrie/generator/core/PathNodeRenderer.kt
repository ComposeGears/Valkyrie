package io.github.composegears.valkyrie.generator.core

import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode
import io.github.composegears.valkyrie.sdk.ir.core.toPathString

fun IrPathNode.asStatement(): String = when (this) {
    is IrPathNode.Close -> "close()"
    is IrPathNode.RelativeMoveTo -> "moveToRelative(${x.formatFloat()}, ${y.formatFloat()})"
    is IrPathNode.MoveTo -> "moveTo(${x.formatFloat()}, ${y.formatFloat()})"
    is IrPathNode.RelativeLineTo -> "lineToRelative(${x.formatFloat()}, ${y.formatFloat()})"
    is IrPathNode.LineTo -> "lineTo(${x.formatFloat()}, ${y.formatFloat()})"
    is IrPathNode.RelativeHorizontalTo -> "horizontalLineToRelative(${x.formatFloat()})"
    is IrPathNode.HorizontalTo -> "horizontalLineTo(${x.formatFloat()})"
    is IrPathNode.RelativeVerticalTo -> "verticalLineToRelative(${y.formatFloat()})"
    is IrPathNode.VerticalTo -> "verticalLineTo(${y.formatFloat()})"
    is IrPathNode.RelativeCurveTo -> {
        "curveToRelative(${dx1.formatFloat()}, ${dy1.formatFloat()}, ${dx2.formatFloat()}, ${dy2.formatFloat()}, ${dx3.formatFloat()}, ${dy3.formatFloat()})"
    }
    is IrPathNode.CurveTo -> {
        "curveTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()}, ${x3.formatFloat()}, ${y3.formatFloat()})"
    }
    is IrPathNode.RelativeReflectiveCurveTo -> {
        "reflectiveCurveToRelative(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
    }
    is IrPathNode.ReflectiveCurveTo -> {
        "reflectiveCurveTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
    }
    is IrPathNode.RelativeQuadTo -> {
        "quadToRelative(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
    }
    is IrPathNode.QuadTo -> {
        "quadTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
    }
    is IrPathNode.RelativeReflectiveQuadTo -> "reflectiveQuadToRelative(${x.formatFloat()}, ${y.formatFloat()})"
    is IrPathNode.ReflectiveQuadTo -> "reflectiveQuadTo(${x.formatFloat()}, ${y.formatFloat()})"
    is IrPathNode.RelativeArcTo -> {
        "arcToRelative(${horizontalEllipseRadius.formatFloat()}, ${verticalEllipseRadius.formatFloat()}, ${theta.formatFloat()}, isMoreThanHalf = $isMoreThanHalf, isPositiveArc = $isPositiveArc, ${arcStartDx.formatFloat()}, ${arcStartDy.formatFloat()})"
    }
    is IrPathNode.ArcTo -> {
        "arcTo(${horizontalEllipseRadius.formatFloat()}, ${verticalEllipseRadius.formatFloat()}, ${theta.formatFloat()}, isMoreThanHalf = $isMoreThanHalf, isPositiveArc = $isPositiveArc, ${arcStartX.formatFloat()}, ${arcStartY.formatFloat()})"
    }
}

fun List<IrPathNode>.asPathDataString(): String = toPathString()
