package io.github.composegears.valkyrie.ir.util

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

fun Char.toPathNodes(args: FloatArray): List<IrPathNode> = when (this) {
    'Z', 'z' -> listOf(Close)
    'm' -> pathNodesFromArgs(args, NUM_MOVE_TO_ARGS) { array ->
        RelativeMoveTo(
            x = array[0],
            y = array[1],
        )
    }
    'M' -> pathNodesFromArgs(args, NUM_MOVE_TO_ARGS) { array ->
        MoveTo(
            x = array[0],
            y = array[1],
        )
    }
    'l' -> pathNodesFromArgs(args, NUM_LINE_TO_ARGS) { array ->
        RelativeLineTo(
            x = array[0],
            y = array[1],
        )
    }
    'L' -> pathNodesFromArgs(args, NUM_LINE_TO_ARGS) { array ->
        LineTo(
            x = array[0],
            y = array[1],
        )
    }
    'h' -> pathNodesFromArgs(args, NUM_HORIZONTAL_TO_ARGS) { array ->
        RelativeHorizontalTo(x = array[0])
    }
    'H' -> pathNodesFromArgs(args, NUM_HORIZONTAL_TO_ARGS) { array ->
        HorizontalTo(x = array[0])
    }
    'v' -> pathNodesFromArgs(args, NUM_VERTICAL_TO_ARGS) { array ->
        RelativeVerticalTo(y = array[0])
    }
    'V' -> pathNodesFromArgs(args, NUM_VERTICAL_TO_ARGS) { array ->
        VerticalTo(y = array[0])
    }
    'c' -> pathNodesFromArgs(args, NUM_CURVE_TO_ARGS) { array ->
        RelativeCurveTo(
            dx1 = array[0],
            dy1 = array[1],
            dx2 = array[2],
            dy2 = array[3],
            dx3 = array[4],
            dy3 = array[5],
        )
    }
    'C' -> pathNodesFromArgs(args, NUM_CURVE_TO_ARGS) { array ->
        CurveTo(
            x1 = array[0],
            y1 = array[1],
            x2 = array[2],
            y2 = array[3],
            x3 = array[4],
            y3 = array[5],
        )
    }
    's' -> pathNodesFromArgs(args, NUM_REFLECTIVE_CURVE_TO_ARGS) { array ->
        RelativeReflectiveCurveTo(
            x1 = array[0],
            y1 = array[1],
            x2 = array[2],
            y2 = array[3],
        )
    }
    'S' -> pathNodesFromArgs(args, NUM_REFLECTIVE_CURVE_TO_ARGS) { array ->
        ReflectiveCurveTo(
            x1 = array[0],
            y1 = array[1],
            x2 = array[2],
            y2 = array[3],
        )
    }
    'q' -> pathNodesFromArgs(args, NUM_QUAD_TO_ARGS) { array ->
        RelativeQuadTo(
            x1 = array[0],
            y1 = array[1],
            x2 = array[2],
            y2 = array[3],
        )
    }
    'Q' -> pathNodesFromArgs(args, NUM_QUAD_TO_ARGS) { array ->
        QuadTo(
            x1 = array[0],
            y1 = array[1],
            x2 = array[2],
            y2 = array[3],
        )
    }
    't' -> pathNodesFromArgs(args, NUM_REFLECTIVE_QUAD_TO_ARGS) { array ->
        RelativeReflectiveQuadTo(
            x = array[0],
            y = array[1],
        )
    }
    'T' -> pathNodesFromArgs(args, NUM_REFLECTIVE_QUAD_TO_ARGS) { array ->
        ReflectiveQuadTo(
            x = array[0],
            y = array[1],
        )
    }
    'a' -> pathNodesFromArgs(args, NUM_ARC_TO_ARGS) { array ->
        RelativeArcTo(
            horizontalEllipseRadius = array[0],
            verticalEllipseRadius = array[1],
            theta = array[2],
            isMoreThanHalf = array[3].compareTo(0.0f) != 0,
            isPositiveArc = array[4].compareTo(0.0f) != 0,
            arcStartDx = array[5],
            arcStartDy = array[6],
        )
    }
    'A' -> pathNodesFromArgs(args, NUM_ARC_TO_ARGS) { array ->
        ArcTo(
            horizontalEllipseRadius = array[0],
            verticalEllipseRadius = array[1],
            theta = array[2],
            isMoreThanHalf = array[3].compareTo(0.0f) != 0,
            isPositiveArc = array[4].compareTo(0.0f) != 0,
            arcStartX = array[5],
            arcStartY = array[6],
        )
    }
    else -> error("Unknown command for: $this")
}

private inline fun pathNodesFromArgs(
    args: FloatArray,
    numArgs: Int,
    nodeFor: (subArray: FloatArray) -> IrPathNode,
): List<IrPathNode> {
    return (0..args.size - numArgs step numArgs).map { index ->
        val subArray = args.slice(index until index + numArgs).toFloatArray()

        // According to the spec, if a MoveTo is followed by multiple pairs of coordinates,
        // the subsequent pairs are treated as implicit corresponding LineTo commands.
        when (val node = nodeFor(subArray)) {
            is MoveTo if index > 0 -> LineTo(subArray[0], subArray[1])
            is RelativeMoveTo if index > 0 -> RelativeLineTo(subArray[0], subArray[1])
            else -> node
        }
    }
}

/**
 * Constants for the number of expected arguments for a given node. If the number of received
 * arguments is a multiple of these, the excess will be converted into additional path nodes.
 */
private const val NUM_MOVE_TO_ARGS = 2
private const val NUM_LINE_TO_ARGS = 2
private const val NUM_HORIZONTAL_TO_ARGS = 1
private const val NUM_VERTICAL_TO_ARGS = 1
private const val NUM_CURVE_TO_ARGS = 6
private const val NUM_REFLECTIVE_CURVE_TO_ARGS = 4
private const val NUM_QUAD_TO_ARGS = 4
private const val NUM_REFLECTIVE_QUAD_TO_ARGS = 2
private const val NUM_ARC_TO_ARGS = 7
