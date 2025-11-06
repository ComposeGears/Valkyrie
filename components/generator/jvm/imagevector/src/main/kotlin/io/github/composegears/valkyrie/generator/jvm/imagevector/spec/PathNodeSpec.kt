package io.github.composegears.valkyrie.generator.jvm.imagevector.spec

import io.github.composegears.valkyrie.generator.core.formatFloat
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.ArcTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.Close
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.CurveTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.HorizontalTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.LineTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.MoveTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.QuadTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.ReflectiveCurveTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.ReflectiveQuadTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeArcTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeCurveTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeHorizontalTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeLineTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeMoveTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeQuadTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeReflectiveCurveTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeReflectiveQuadTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.RelativeVerticalTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.VerticalTo

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
    is RelativeCurveTo -> {
        "curveToRelative(${dx1.formatFloat()}, ${dy1.formatFloat()}, ${dx2.formatFloat()}, ${dy2.formatFloat()}, ${dx3.formatFloat()}, ${dy3.formatFloat()})"
    }
    is CurveTo -> {
        "curveTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()}, ${x3.formatFloat()}, ${y3.formatFloat()})"
    }
    is RelativeReflectiveCurveTo -> {
        "reflectiveCurveToRelative(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
    }
    is ReflectiveCurveTo -> {
        "reflectiveCurveTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
    }
    is RelativeQuadTo -> {
        "quadToRelative(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
    }
    is QuadTo -> {
        "quadTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
    }
    is RelativeReflectiveQuadTo -> "reflectiveQuadToRelative(${x.formatFloat()}, ${y.formatFloat()})"
    is ReflectiveQuadTo -> "reflectiveQuadTo(${x.formatFloat()}, ${y.formatFloat()})"
    is RelativeArcTo -> {
        "arcToRelative(${horizontalEllipseRadius.formatFloat()}, ${verticalEllipseRadius.formatFloat()}, ${theta.formatFloat()}, isMoreThanHalf = $isMoreThanHalf, isPositiveArc = $isPositiveArc, ${arcStartDx.formatFloat()}, ${arcStartDy.formatFloat()})"
    }
    is ArcTo -> {
        "arcTo(${horizontalEllipseRadius.formatFloat()}, ${verticalEllipseRadius.formatFloat()}, ${theta.formatFloat()}, isMoreThanHalf = $isMoreThanHalf, isPositiveArc = $isPositiveArc, ${arcStartX.formatFloat()}, ${arcStartY.formatFloat()})"
    }
}
