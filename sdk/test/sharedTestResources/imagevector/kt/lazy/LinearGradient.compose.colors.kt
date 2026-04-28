package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val LinearGradientComposeColors: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "LinearGradientComposeColors",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.White,
                    1f to Color(0xFFF1E5FC),
                ),
                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                end = androidx.compose.ui.geometry.Offset(24f, 0f),
            ),
        ) {
            moveTo(0f, 0f)
            lineTo(24f, 0f)
            lineTo(24f, 12f)
            lineTo(0f, 12f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.White.copy(alpha = 0.7490196f),
                    1f to Color.Black,
                ),
                start = androidx.compose.ui.geometry.Offset(0f, 12f),
                end = androidx.compose.ui.geometry.Offset(0f, 24f),
            ),
        ) {
            moveTo(0f, 12f)
            lineTo(24f, 12f)
            lineTo(24f, 24f)
            lineTo(0f, 24f)
            close()
        }
    }.build()
}

