package io.github.composegears.valkyrie.sdk.ir.util

import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.util.internal.ColorClassification
import io.github.composegears.valkyrie.sdk.ir.util.internal.DominantShade

val IrImageVector.aspectRatio: Float
    get() = if (viewportHeight != 0f && viewportWidth != 0f) {
        viewportWidth / viewportHeight
    } else {
        1f
    }

val IrImageVector.dominantShadeColor: DominantShade
    get() = ColorClassification.from(this)
