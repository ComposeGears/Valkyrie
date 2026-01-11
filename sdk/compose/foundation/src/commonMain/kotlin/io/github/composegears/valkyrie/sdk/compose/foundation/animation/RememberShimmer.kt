package io.github.composegears.valkyrie.sdk.compose.foundation.animation

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Container holding shared shimmer animation state. */
class Shimmer internal constructor(
    internal val translateState: State<Float>,
    internal val gradientWidthFactor: Float,
)

/**
 * Remember a single shimmer animation instance you can pass to many items.
 * Usage:
 * val shimmer = rememberShimmer()
 * LazyColumn { items(data) { Row(Modifier.shimmer(shimmer).height(48.dp)) } }
 */
@Composable
fun rememberShimmer(
    gradientWidthFactor: Float = 1.5f,
    durationMillis: Int = 2200,
    easing: Easing = FastOutSlowInEasing,
    initialTranslate: Float = -400f,
    targetTranslate: Float = 1200f,
): Shimmer {
    val transition = rememberInfiniteTransition(label = "RememberShimmer")
    val translateState = transition.animateFloat(
        initialValue = initialTranslate,
        targetValue = targetTranslate,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = easing),
        ),
        label = "RememberShimmerTranslate",
    )
    return Shimmer(translateState, gradientWidthFactor)
}

/** Apply a remembered shimmer to this modifier. */
@Composable
fun Modifier.shimmer(
    shimmer: Shimmer,
    cornerRadius: Dp = 0.dp,
    isLoading: Boolean = true,
    colors: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.8f),
        Color.LightGray.copy(alpha = 0.3f),
    ),
): Modifier {
    if (!isLoading) return this

    return drawWithContent {
        val translate = shimmer.translateState.value
        val cornerPx = cornerRadius.toPx()
        val brush = Brush.linearGradient(
            colors = colors,
            start = Offset(translate, 0f),
            end = Offset(translate + size.width / shimmer.gradientWidthFactor, size.height),
        )
        drawRoundRect(
            brush = brush,
            cornerRadius = CornerRadius(cornerPx, cornerPx),
            size = size,
        )
    }
}
