package io.github.composegears.valkyrie.ir.util

import io.github.composegears.valkyrie.ir.IrImageVector

val IrImageVector.aspectRatio: Float
    get() = if (viewportHeight != 0f && viewportWidth != 0f) {
        viewportWidth / viewportHeight
    } else {
        1f
    }

val IrImageVector.dominantShadeColor: DominantShade
    get() = ColorClassification.from(iconColors())
