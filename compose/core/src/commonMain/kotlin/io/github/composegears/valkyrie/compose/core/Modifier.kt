package io.github.composegears.valkyrie.compose.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 1.dp,
    dashWidth: Dp = 4.dp,
    gapWidth: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round,
) = this.drawWithContent {
    val outline = shape.createOutline(size, layoutDirection, this)

    val stroke = Stroke(
        cap = cap,
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
        ),
    )

    drawContent()
    drawOutline(
        outline = outline,
        style = stroke,
        brush = SolidColor(color),
    )
}

@Suppress("ktlint:compose:modifier-composed-check")
fun Modifier.clickableNoRipple(onClick: () -> Unit, enabled: Boolean = true) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClick,
    )
}
