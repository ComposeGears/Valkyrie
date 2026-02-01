package io.github.composegears.valkyrie.jewel.graphics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 1.dp,
    dashWidth: Dp = 4.dp,
    gapWidth: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Round,
) = drawWithContent {
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

@Preview
@Composable
private fun DashedBorderPreview() = PreviewTheme {
    Box(
        modifier = Modifier
            .align(alignment = Alignment.Center)
            .size(200.dp)
            .dashedBorder(
                color = JewelTheme.contentColor,
                shape = RoundedCornerShape(4.dp),
            ),
    ) {
    }
}
