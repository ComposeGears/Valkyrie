package io.github.composegears.valkyrie.parser.ktfile.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrPathNode
import io.github.composegears.valkyrie.ir.IrPathNode.ArcTo
import io.github.composegears.valkyrie.ir.IrPathNode.Close
import io.github.composegears.valkyrie.ir.IrPathNode.CurveTo
import io.github.composegears.valkyrie.ir.IrPathNode.HorizontalTo
import io.github.composegears.valkyrie.ir.IrPathNode.LineTo
import io.github.composegears.valkyrie.ir.IrPathNode.MoveTo
import io.github.composegears.valkyrie.ir.IrPathNode.QuadTo
import io.github.composegears.valkyrie.ir.IrPathNode.ReflectiveCurveTo
import io.github.composegears.valkyrie.ir.IrPathNode.ReflectiveQuadTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeArcTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeCurveTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeHorizontalTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeLineTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeMoveTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeQuadTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeReflectiveCurveTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeReflectiveQuadTo
import io.github.composegears.valkyrie.ir.IrPathNode.RelativeVerticalTo
import io.github.composegears.valkyrie.ir.IrPathNode.VerticalTo
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode.IrGroup
import io.github.composegears.valkyrie.ir.IrVectorNode.IrPath

fun IrImageVector.toComposeImageVector(
    defaultWidth: Dp = this.defaultWidth.dp,
    defaultHeight: Dp = this.defaultHeight.dp,
): ImageVector {
    return ImageVector.Builder(
        name = name,
        defaultWidth = defaultWidth,
        defaultHeight = defaultHeight,
        viewportWidth = viewportWidth,
        viewportHeight = viewportHeight,
        autoMirror = autoMirror,
    ).apply {
        nodes.forEach {
            when (it) {
                is IrGroup -> addGroup(it)
                is IrPath -> addPath(it)
            }
        }
    }.build()
}

private fun ImageVector.Builder.addGroup(group: IrGroup) {
    group(
        name = group.name,
        rotate = group.rotate,
        pivotX = group.pivotX,
        pivotY = group.pivotY,
        scaleX = group.scaleX,
        scaleY = group.scaleY,
        translationX = group.translationX,
        translationY = group.translationY,
        clipPathData = group.clipPathData.buildClipPath(),
    ) {
        group.paths.forEach {
            addPath(it)
        }
    }
}

private fun ImageVector.Builder.addPath(path: IrPath) {
    path(
        name = path.name,
        fill = path.fill.toFill(),
        fillAlpha = path.fillAlpha,
        stroke = path.stroke.toBrush(),
        strokeAlpha = path.strokeAlpha,
        strokeLineWidth = path.strokeLineWidth,
        strokeLineCap = path.strokeLineCap.toLineCap(),
        strokeLineJoin = path.strokeLineJoin.toStrokeJoin(),
        strokeLineMiter = path.strokeLineMiter,
        pathFillType = path.pathFillType.toFillType(),
        pathBuilder = {
            buildPath(path.paths)
        },
    )
}

private fun List<IrPathNode>.buildClipPath(): List<PathNode> = PathData {
    buildPath(this@buildClipPath)
}

private fun PathBuilder.buildPath(paths: List<IrPathNode>) {
    paths.forEach { node ->
        when (node) {
            is ArcTo -> arcTo(
                horizontalEllipseRadius = node.horizontalEllipseRadius,
                verticalEllipseRadius = node.verticalEllipseRadius,
                theta = node.theta,
                isMoreThanHalf = node.isMoreThanHalf,
                isPositiveArc = node.isPositiveArc,
                x1 = node.arcStartX,
                y1 = node.arcStartY,
            )
            is Close -> close()
            is CurveTo -> curveTo(
                x1 = node.x1,
                y1 = node.y1,
                x2 = node.x2,
                y2 = node.y2,
                x3 = node.x3,
                y3 = node.y3,
            )
            is HorizontalTo -> horizontalLineTo(x = node.x)
            is LineTo -> lineTo(x = node.x, y = node.y)
            is MoveTo -> moveTo(x = node.x, y = node.y)
            is QuadTo -> quadTo(
                x1 = node.x1,
                y1 = node.y1,
                x2 = node.x2,
                y2 = node.y2,
            )
            is ReflectiveCurveTo -> reflectiveCurveTo(
                x1 = node.x1,
                y1 = node.y1,
                x2 = node.x2,
                y2 = node.y2,
            )
            is ReflectiveQuadTo -> reflectiveQuadTo(
                x1 = node.x,
                y1 = node.y,
            )
            is RelativeArcTo -> arcToRelative(
                a = node.horizontalEllipseRadius,
                b = node.verticalEllipseRadius,
                theta = node.theta,
                isMoreThanHalf = node.isMoreThanHalf,
                isPositiveArc = node.isPositiveArc,
                dx1 = node.arcStartDx,
                dy1 = node.arcStartDy,
            )
            is RelativeCurveTo -> curveToRelative(
                dx1 = node.dx1,
                dy1 = node.dy1,
                dx2 = node.dx2,
                dy2 = node.dy2,
                dx3 = node.dx3,
                dy3 = node.dy3,
            )
            is RelativeHorizontalTo -> horizontalLineToRelative(dx = node.x)
            is RelativeLineTo -> lineToRelative(dx = node.x, dy = node.y)
            is RelativeMoveTo -> moveToRelative(dx = node.x, dy = node.y)
            is RelativeQuadTo -> quadToRelative(
                dx1 = node.x1,
                dy1 = node.y1,
                dx2 = node.x2,
                dy2 = node.y2,
            )
            is RelativeReflectiveCurveTo -> reflectiveCurveToRelative(
                dx1 = node.x1,
                dy1 = node.y1,
                dx2 = node.x2,
                dy2 = node.y2,
            )
            is RelativeReflectiveQuadTo -> reflectiveQuadToRelative(
                dx1 = node.x,
                dy1 = node.y,
            )
            is RelativeVerticalTo -> verticalLineToRelative(dy = node.y)
            is VerticalTo -> verticalLineTo(y = node.y)
        }
    }
}

private fun IrStroke?.toBrush(): Brush? {
    return when (this) {
        is IrStroke.Color -> SolidColor(Color(color = irColor.argb))
        else -> null
    }
}

private fun IrFill?.toFill(): Brush? {
    return when (this) {
        is IrFill.Color -> return SolidColor(Color(color = irColor.argb))
        is IrFill.LinearGradient -> {
            Brush.linearGradient(
                colorStops = colorStops.map { colorStop ->
                    colorStop.offset to Color(colorStop.irColor.argb)
                }.toTypedArray(),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
            )
        }
        is IrFill.RadialGradient -> {
            Brush.radialGradient(
                colorStops = colorStops.map { colorStop ->
                    colorStop.offset to Color(colorStop.irColor.argb)
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
