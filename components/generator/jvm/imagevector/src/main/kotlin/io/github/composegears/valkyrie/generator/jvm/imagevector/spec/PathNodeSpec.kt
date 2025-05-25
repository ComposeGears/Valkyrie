package io.github.composegears.valkyrie.generator.jvm.imagevector.spec

import io.github.composegears.valkyrie.generator.core.formatFloat
import io.github.composegears.valkyrie.ir.IrPathNode
import io.github.composegears.valkyrie.ir.IrPathNode.Close
import io.github.composegears.valkyrie.ir.IrPathNode.HorizontalTo
import io.github.composegears.valkyrie.ir.IrPathNode.LineTo
import io.github.composegears.valkyrie.ir.IrPathNode.MoveTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeHorizontalTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeLineTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeMoveTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeVerticalTo
import io.github.composegears.valkyrie.ir.IrPathNode.VerticalTo

internal fun IrPathNode.asStatement(): String = when (this) {
    is Close -> "close()"
    is RelativeMoveTo -> "moveToRelative(${x.formatFloat()}, ${y.formatFloat()})"
    is MoveTo -> "moveTo(${x.formatFloat()}, ${y.formatFloat()})"
    is RelativeLineTo -> "lineToRelative(${x.formatFloat()}, ${y.formatFloat()})"
    is LineTo -> "lineTo(${x.formatFloat()}, ${y.formatFloat()})"
    is RelativeHorizontalTo -> "horizontalLineToRelative(${x.formatFloat()})"
    is HorizontalTo -> "horizontalLineTo(${x.formatFloat()})"
    is RelativeVerticalTo -> "verticalLineToRelative(${y.formatFloat()})"
    is VerticalTo -> "verticalLineTo(${y.formatFloat()})"
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
