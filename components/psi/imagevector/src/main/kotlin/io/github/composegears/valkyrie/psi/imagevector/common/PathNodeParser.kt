package io.github.composegears.valkyrie.psi.imagevector.common

import io.github.composegears.valkyrie.psi.extension.childrenOfType
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
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression

internal fun KtCallExpression.parseClipPath(): List<IrPathNode> {
    val clipPathArg = valueArguments
        .find { it.getArgumentName()?.asName?.identifier == "clipPathData" } ?: return emptyList()

    return clipPathArg
        .childrenOfType<KtCallExpression>()
        .toList()
        .parsePathNodes()
}

internal fun KtBlockExpression.parsePath(): List<IrPathNode> {
    return statements
        .filterIsInstance<KtCallExpression>()
        .parsePathNodes()
}

private fun List<KtCallExpression>.parsePathNodes(): List<IrPathNode> {
    val pathNodes = mutableListOf<IrPathNode>()

    forEach { expression ->
        val args = expression.valueArguments.mapNotNull { it.getArgumentExpression()?.text }
        when (expression.calleeExpression?.text) {
            "close" -> pathNodes += Close
            "moveToRelative" -> if (args.size == 2 && args.allFloat()) {
                pathNodes += RelativeMoveTo(
                    x = args[0].toFloat(),
                    y = args[1].toFloat(),
                )
            }
            "moveTo" -> if (args.size == 2 && args.allFloat()) {
                pathNodes += MoveTo(
                    x = args[0].toFloat(),
                    y = args[1].toFloat(),
                )
            }
            "lineToRelative" -> if (args.size == 2 && args.allFloat()) {
                pathNodes += RelativeLineTo(
                    x = args[0].toFloat(),
                    y = args[1].toFloat(),
                )
            }
            "lineTo" -> if (args.size == 2 && args.allFloat()) {
                pathNodes += LineTo(
                    x = args[0].toFloat(),
                    y = args[1].toFloat(),
                )
            }
            "horizontalLineToRelative" -> if (args.size == 1 && args.allFloat()) {
                pathNodes += RelativeHorizontalTo(x = args[0].toFloat())
            }
            "horizontalLineTo" -> if (args.size == 1 && args.allFloat()) {
                pathNodes += HorizontalTo(x = args[0].toFloat())
            }
            "verticalLineToRelative" -> if (args.size == 1 && args.allFloat()) {
                pathNodes += RelativeVerticalTo(y = args[0].toFloat())
            }
            "verticalLineTo" -> if (args.size == 1 && args.allFloat()) {
                pathNodes += VerticalTo(y = args[0].toFloat())
            }
            "curveToRelative" -> if (args.size == 6 && args.allFloat()) {
                pathNodes += RelativeCurveTo(
                    dx1 = args[0].toFloat(),
                    dy1 = args[1].toFloat(),
                    dx2 = args[2].toFloat(),
                    dy2 = args[3].toFloat(),
                    dx3 = args[4].toFloat(),
                    dy3 = args[5].toFloat(),
                )
            }
            "curveTo" -> if (args.size == 6 && args.allFloat()) {
                pathNodes += CurveTo(
                    x1 = args[0].toFloat(),
                    y1 = args[1].toFloat(),
                    x2 = args[2].toFloat(),
                    y2 = args[3].toFloat(),
                    x3 = args[4].toFloat(),
                    y3 = args[5].toFloat(),
                )
            }
            "reflectiveCurveToRelative" -> if (args.size == 4 && args.allFloat()) {
                pathNodes += RelativeReflectiveCurveTo(
                    x1 = args[0].toFloat(),
                    y1 = args[1].toFloat(),
                    x2 = args[2].toFloat(),
                    y2 = args[3].toFloat(),
                )
            }
            "reflectiveCurveTo" -> if (args.size == 4 && args.allFloat()) {
                pathNodes += ReflectiveCurveTo(
                    x1 = args[0].toFloat(),
                    y1 = args[1].toFloat(),
                    x2 = args[2].toFloat(),
                    y2 = args[3].toFloat(),
                )
            }
            "quadToRelative" -> if (args.size == 4 && args.allFloat()) {
                pathNodes += RelativeQuadTo(
                    x1 = args[0].toFloat(),
                    y1 = args[1].toFloat(),
                    x2 = args[2].toFloat(),
                    y2 = args[3].toFloat(),
                )
            }
            "quadTo" -> if (args.size == 4 && args.allFloat()) {
                pathNodes += QuadTo(
                    x1 = args[0].toFloat(),
                    y1 = args[1].toFloat(),
                    x2 = args[2].toFloat(),
                    y2 = args[3].toFloat(),
                )
            }
            "reflectiveQuadToRelative" -> if (args.size == 2 && args.allFloat()) {
                pathNodes += RelativeReflectiveQuadTo(
                    x = args[0].toFloat(),
                    y = args[1].toFloat(),
                )
            }
            "reflectiveQuadTo" -> if (args.size == 2 && args.allFloat()) {
                pathNodes += ReflectiveQuadTo(
                    x = args[0].toFloat(),
                    y = args[1].toFloat(),
                )
            }
            "arcToRelative" -> if (args.size == 7 && args.allFloatOrBoolean()) {
                pathNodes += RelativeArcTo(
                    horizontalEllipseRadius = args[0].toFloat(),
                    verticalEllipseRadius = args[1].toFloat(),
                    theta = args[2].toFloat(),
                    isMoreThanHalf = args[3].toBoolean(),
                    isPositiveArc = args[4].toBoolean(),
                    arcStartDx = args[5].toFloat(),
                    arcStartDy = args[6].toFloat(),
                )
            }
            "arcTo" -> if (args.size == 7 && args.allFloatOrBoolean()) {
                pathNodes += ArcTo(
                    horizontalEllipseRadius = args[0].toFloat(),
                    verticalEllipseRadius = args[1].toFloat(),
                    theta = args[2].toFloat(),
                    isMoreThanHalf = args[3].toBoolean(),
                    isPositiveArc = args[4].toBoolean(),
                    arcStartX = args[5].toFloat(),
                    arcStartY = args[6].toFloat(),
                )
            }
        }
    }

    return pathNodes
}

private fun List<String>.allFloat() = all(String::isFloat)
private fun List<String>.allFloatOrBoolean() = all(String::isFloatOrBoolean)

private fun String.isFloat() = toFloatOrNull() != null

private fun String.isFloatOrBoolean() = isFloat() || this == "true" || this == "false" || this == "0" || this == "1"
