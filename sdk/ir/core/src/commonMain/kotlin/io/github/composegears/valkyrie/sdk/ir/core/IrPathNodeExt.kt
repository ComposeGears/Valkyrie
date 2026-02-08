package io.github.composegears.valkyrie.sdk.ir.core

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

/**
 * Extension function to convert a list of IrPathNodes to a complete path data string.
 */
fun List<IrPathNode>.toPathString(formatFloat: (Float) -> String = { it.toString() }): String {
    return joinToString(" ") { it.toPathString(formatFloat) }
}

/**
 * Extension function to convert an IrPathNode back to its path string representation.
 */
private fun IrPathNode.toPathString(formatFloat: (Float) -> String = { it.toString() }): String {
    val command = toPathCommand()
    val args = toPathArgs()

    return if (args.isEmpty()) {
        command.toString()
    } else {
        val isArc = command == 'A' || command == 'a'
        buildString {
            append(command)
            args.forEachIndexed { index, arg ->
                append(' ')
                if (isArc && (index == 3 || index == 4)) {
                    append(if (arg != 0f) '1' else '0')
                } else {
                    append(formatFloat(arg))
                }
            }
        }
    }
}

/**
 * Extension function to convert an IrPathNode back to its argument array.
 */
private fun IrPathNode.toPathArgs(): FloatArray = when (this) {
    is Close -> floatArrayOf()
    is MoveTo -> floatArrayOf(x, y)
    is RelativeMoveTo -> floatArrayOf(x, y)
    is LineTo -> floatArrayOf(x, y)
    is RelativeLineTo -> floatArrayOf(x, y)
    is HorizontalTo -> floatArrayOf(x)
    is RelativeHorizontalTo -> floatArrayOf(x)
    is VerticalTo -> floatArrayOf(y)
    is RelativeVerticalTo -> floatArrayOf(y)
    is CurveTo -> floatArrayOf(x1, y1, x2, y2, x3, y3)
    is RelativeCurveTo -> floatArrayOf(dx1, dy1, dx2, dy2, dx3, dy3)
    is ReflectiveCurveTo -> floatArrayOf(x1, y1, x2, y2)
    is RelativeReflectiveCurveTo -> floatArrayOf(x1, y1, x2, y2)
    is QuadTo -> floatArrayOf(x1, y1, x2, y2)
    is RelativeQuadTo -> floatArrayOf(x1, y1, x2, y2)
    is ReflectiveQuadTo -> floatArrayOf(x, y)
    is RelativeReflectiveQuadTo -> floatArrayOf(x, y)
    is ArcTo -> floatArrayOf(
        horizontalEllipseRadius,
        verticalEllipseRadius,
        theta,
        if (isMoreThanHalf) 1f else 0f,
        if (isPositiveArc) 1f else 0f,
        arcStartX,
        arcStartY,
    )
    is RelativeArcTo -> floatArrayOf(
        horizontalEllipseRadius,
        verticalEllipseRadius,
        theta,
        if (isMoreThanHalf) 1f else 0f,
        if (isPositiveArc) 1f else 0f,
        arcStartDx,
        arcStartDy,
    )
}

private fun IrPathNode.toPathCommand(): Char = when (this) {
    is Close -> 'Z'
    is MoveTo -> 'M'
    is RelativeMoveTo -> 'm'
    is LineTo -> 'L'
    is RelativeLineTo -> 'l'
    is HorizontalTo -> 'H'
    is RelativeHorizontalTo -> 'h'
    is VerticalTo -> 'V'
    is RelativeVerticalTo -> 'v'
    is CurveTo -> 'C'
    is RelativeCurveTo -> 'c'
    is ReflectiveCurveTo -> 'S'
    is RelativeReflectiveCurveTo -> 's'
    is QuadTo -> 'Q'
    is RelativeQuadTo -> 'q'
    is ReflectiveQuadTo -> 'T'
    is RelativeReflectiveQuadTo -> 't'
    is ArcTo -> 'A'
    is RelativeArcTo -> 'a'
}
