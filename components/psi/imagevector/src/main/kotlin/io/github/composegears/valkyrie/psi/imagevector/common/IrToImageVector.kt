package io.github.composegears.valkyrie.psi.imagevector.common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.extensions.toColorInt
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrPathNode
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode.IrGroup
import io.github.composegears.valkyrie.ir.IrVectorNode.IrPath

internal fun IrImageVector.toComposeImageVector(): ImageVector {
    return ImageVector.Builder(
        name = name,
        defaultWidth = defaultWidth.dp,
        defaultHeight = defaultHeight.dp,
        viewportWidth = viewportWidth,
        viewportHeight = viewportHeight,
        autoMirror = autoMirror,
    ).apply {
        vectorNodes.forEach {
            when (it) {
                is IrGroup -> addGroup(it)
                is IrPath -> addPath(it)
            }
        }
    }.build()
}

private fun ImageVector.Builder.addGroup(group: IrGroup) {
    group {
        group.nodes.forEach {
            addPath(it)
        }
    }
}

private fun ImageVector.Builder.addPath(path: IrPath) {
    path(
        fill = path.fill.toFill(),
        fillAlpha = path.fillAlpha,
        stroke = path.stroke.toBrush(),
        strokeAlpha = path.strokeAlpha,
        strokeLineWidth = path.strokeLineWidth,
        strokeLineCap = path.strokeLineCap.toLineCap(),
        strokeLineJoin = path.strokeLineJoin.toStrokeJoin(),
        strokeLineMiter = path.strokeLineMiter,
        pathFillType = path.pathFillType.toFillType(),
    ) {
        path.nodes.forEach { node ->
            when (node) {
                is IrPathNode.ArcTo -> arcTo(
                    horizontalEllipseRadius = node.horizontalEllipseRadius,
                    verticalEllipseRadius = node.verticalEllipseRadius,
                    theta = node.theta,
                    isMoreThanHalf = node.isMoreThanHalf,
                    isPositiveArc = node.isPositiveArc,
                    x1 = node.arcStartX,
                    y1 = node.arcStartY,
                )
                is IrPathNode.Close -> close()
                is IrPathNode.CurveTo -> curveTo(
                    x1 = node.x1,
                    y1 = node.y1,
                    x2 = node.x2,
                    y2 = node.y2,
                    x3 = node.x3,
                    y3 = node.y3,
                )
                is IrPathNode.HorizontalTo -> horizontalLineTo(x = node.x)
                is IrPathNode.LineTo -> lineTo(x = node.x, y = node.y)
                is IrPathNode.MoveTo -> moveTo(x = node.x, y = node.y)
                is IrPathNode.QuadTo -> quadTo(
                    x1 = node.x1,
                    y1 = node.y1,
                    x2 = node.x2,
                    y2 = node.y2,
                )
                is IrPathNode.ReflectiveCurveTo -> reflectiveCurveTo(
                    x1 = node.x1,
                    y1 = node.y1,
                    x2 = node.x2,
                    y2 = node.y2,
                )
                is IrPathNode.ReflectiveQuadTo -> reflectiveQuadTo(
                    x1 = node.x,
                    y1 = node.y,
                )
                is IrPathNode.RelativeArcTo -> arcToRelative(
                    a = node.horizontalEllipseRadius,
                    b = node.verticalEllipseRadius,
                    theta = node.theta,
                    isMoreThanHalf = node.isMoreThanHalf,
                    isPositiveArc = node.isPositiveArc,
                    dx1 = node.arcStartDx,
                    dy1 = node.arcStartDy,
                )
                is IrPathNode.RelativeCurveTo -> curveToRelative(
                    dx1 = node.dx1,
                    dy1 = node.dy1,
                    dx2 = node.dx2,
                    dy2 = node.dy2,
                    dx3 = node.dx3,
                    dy3 = node.dy3,
                )
                is IrPathNode.RelativeHorizontalTo -> horizontalLineToRelative(dx = node.x)
                is IrPathNode.RelativeLineTo -> lineToRelative(dx = node.x, dy = node.y)
                is IrPathNode.RelativeMoveTo -> moveToRelative(dx = node.x, dy = node.y)
                is IrPathNode.RelativeQuadTo -> quadToRelative(
                    dx1 = node.x1,
                    dy1 = node.y1,
                    dx2 = node.x2,
                    dy2 = node.y2,
                )
                is IrPathNode.RelativeReflectiveCurveTo -> reflectiveCurveToRelative(
                    dx1 = node.x1,
                    dy1 = node.y1,
                    dx2 = node.x2,
                    dy2 = node.y2,
                )
                is IrPathNode.RelativeReflectiveQuadTo -> reflectiveQuadToRelative(
                    dx1 = node.x,
                    dy1 = node.y,
                )
                is IrPathNode.RelativeVerticalTo -> verticalLineToRelative(dy = node.y)
                is IrPathNode.VerticalTo -> verticalLineTo(y = node.y)
            }
        }
    }
}

private fun IrStroke?.toBrush(): Brush? {
    return when (this) {
        is IrStroke.Color -> SolidColor(Color(color = colorHex.toColorInt()))
        else -> null
    }
}

private fun IrFill?.toFill(): Brush? {
    return when (this) {
        is IrFill.Color -> return SolidColor(Color(color = colorHex.toColorInt()))
        is IrFill.LinearGradient -> {
            Brush.linearGradient(
                colorStops = colorStops.map { colorStop ->
                    colorStop.offset to Color(colorStop.color.toColorInt())
                }.toTypedArray(),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
            )
        }
        is IrFill.RadialGradient -> {
            Brush.radialGradient(
                colorStops = colorStops.map { colorStop ->
                    colorStop.offset to Color(colorStop.color.toColorInt())
                }.toTypedArray(),
                center = Offset(centerX, centerY),
                radius = radius,
            )
        }
        else -> null
    }
}

private fun IrStrokeLineCap.toLineCap(): StrokeCap {
    return when (this) {
        IrStrokeLineCap.Butt -> StrokeCap.Butt
        IrStrokeLineCap.Round -> StrokeCap.Round
        IrStrokeLineCap.Square -> StrokeCap.Square
    }
}

private fun IrStrokeLineJoin.toStrokeJoin(): StrokeJoin {
    return when (this) {
        IrStrokeLineJoin.Bevel -> StrokeJoin.Bevel
        IrStrokeLineJoin.Miter -> StrokeJoin.Miter
        IrStrokeLineJoin.Round -> StrokeJoin.Round
    }
}

private fun IrPathFillType.toFillType(): PathFillType {
    return when (this) {
        IrPathFillType.EvenOdd -> PathFillType.EvenOdd
        IrPathFillType.NonZero -> PathFillType.NonZero
    }
}
