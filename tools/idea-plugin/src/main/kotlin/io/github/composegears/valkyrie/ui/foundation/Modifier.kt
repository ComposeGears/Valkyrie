package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.layer.drawLayer

fun Modifier.blendMode(blendMode: BlendMode): Modifier {
    return this.drawWithCache {
        val graphicsLayer = obtainGraphicsLayer()
        graphicsLayer.apply {
            record {
                drawContent()
            }
            this.blendMode = blendMode
        }
        onDrawWithContent {
            drawLayer(graphicsLayer)
        }
    }
}
