package io.github.composegears.valkyrie.sdk.ir.util.internal

import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import io.github.composegears.valkyrie.sdk.ir.util.internal.DominantShade.Black
import io.github.composegears.valkyrie.sdk.ir.util.internal.DominantShade.Mixed
import io.github.composegears.valkyrie.sdk.ir.util.internal.DominantShade.White

enum class DominantShade {
    Black,
    White,
    Mixed,
}

internal object ColorClassification {

    private const val BLACK_THRESHOLD = 0.2f
    private const val WHITE_THRESHOLD = 0.8f

    fun from(imageVector: IrImageVector): DominantShade = from(imageVector.iconColors())

    fun from(colors: List<IrColor>): DominantShade {
        var blackCount = 0
        var whiteCount = 0

        for (color in colors) {
            val r = color.red.toInt() / 255.0f
            val g = color.green.toInt() / 255.0f
            val b = color.blue.toInt() / 255.0f

            val luminance = 0.2126f * r + 0.7152f * g + 0.0722f * b

            if (luminance <= BLACK_THRESHOLD) {
                blackCount++
            } else if (luminance >= WHITE_THRESHOLD) {
                whiteCount++
            }

            // Break early if mix of shades detected
            if (blackCount > 0 && whiteCount > 0) {
                return Mixed
            }
        }

        return when {
            blackCount == colors.size -> Black
            whiteCount == colors.size -> White
            else -> Mixed
        }
    }
}

internal fun IrImageVector.iconColors(): List<IrColor> {
    val colors = mutableSetOf<IrColor>()

    nodes.onEach { node ->
        visitNode(node = node, colors = colors)
    }

    return colors.toList()
}

private fun visitNode(node: IrVectorNode, colors: MutableSet<IrColor>) {
    when (node) {
        is IrVectorNode.IrGroup -> {
            node.nodes.forEach {
                visitNode(node = it, colors = colors)
            }
        }
        is IrVectorNode.IrPath -> visitPath(node, colors)
    }
}

private fun visitPath(
    node: IrVectorNode.IrPath,
    colors: MutableSet<IrColor>,
) {
    when (val fill = node.fill) {
        is IrFill.Color -> colors += fill.irColor
        is IrFill.LinearGradient -> colors += fill.colorStops.map { it.irColor }
        is IrFill.RadialGradient -> colors += fill.colorStops.map { it.irColor }
        null -> {}
    }
    when (val stroke = node.stroke) {
        is IrStroke.Color -> colors += stroke.irColor
        null -> {}
    }
}
