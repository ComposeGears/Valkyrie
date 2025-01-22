package io.github.composegears.valkyrie.ir.util

import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.util.DominantShade.Black
import io.github.composegears.valkyrie.ir.util.DominantShade.Mixed
import io.github.composegears.valkyrie.ir.util.DominantShade.White

enum class DominantShade {
    Black,
    White,
    Mixed,
}

object ColorClassification {

    private const val BLACK_THRESHOLD = 0.2f
    private const val WHITE_THRESHOLD = 0.8f

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
