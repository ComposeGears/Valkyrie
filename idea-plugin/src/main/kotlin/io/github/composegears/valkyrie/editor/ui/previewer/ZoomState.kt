package io.github.composegears.valkyrie.editor.ui.previewer

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp

@Composable
fun rememberZoomState() = remember { ZoomState() }

class ZoomState {
    var maxPreviewSize by mutableStateOf(Dp.Unspecified)

    private val animatable = Animatable(0f)
    val scale by animatable.asState()

    suspend fun animateToScale(targetScale: Float) {
        animatable.animateTo(targetScale)
    }

    suspend fun setScale(targetScale: Float) {
        animatable.snapTo(targetScale)
    }

    suspend fun zoomIn() {
        animateToScale(scale + 1f)
    }

    suspend fun zoomOut() {
        if (scale - 1f > 0f) {
            animateToScale(scale - 1f)
        }
    }

    suspend fun reset() = animateToScale(1f)
}
