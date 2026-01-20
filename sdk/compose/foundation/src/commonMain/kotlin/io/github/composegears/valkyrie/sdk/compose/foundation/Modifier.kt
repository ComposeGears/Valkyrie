package io.github.composegears.valkyrie.sdk.compose.foundation

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Matrix33

inline fun Modifier.applyIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
): Modifier {
    return if (condition) modifier() else this
}

@Composable
fun Modifier.animatedBorder(
    colors: List<Color>,
    shape: Shape = RectangleShape,
    borderWidth: Dp = 2.dp,
    durationInMillis: Int = 1000,
    easing: Easing = LinearEasing,
): Modifier {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationInMillis, easing = easing),
            repeatMode = RepeatMode.Restart,
        ),
    )

    val shader = remember(angle, colors) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val x = size.width / 2f
                val y = size.height / 2f

                return SweepGradientShader(
                    center = Offset(x, y),
                    colors = colors,
                ).makeWithLocalMatrix(
                    localMatrix = Matrix33.makeRotate(
                        deg = angle,
                        pivotx = x,
                        pivoty = y,
                    ),
                )
            }
        }
    }

    return this
        .drawWithCache {
            onDrawWithContent {
                drawContent()
                drawOutline(
                    outline = shape.createOutline(size, layoutDirection, this),
                    style = Stroke(width = borderWidth.toPx()),
                    brush = shader,
                )
            }
        }
}
