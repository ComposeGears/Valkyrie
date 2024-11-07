package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val LinearGradientWithStroke: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "LinearGradientWithStroke",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0.097f to Color(0xFF0095D5),
                    0.301f to Color(0xFF238AD9),
                    0.621f to Color(0xFF557BDE),
                    0.864f to Color(0xFF7472E2),
                    1f to Color(0xFF806EE3)
                ),
                start = Offset(6.384f, 29.605f),
                end = Offset(17.723f, 18.267f)
            )
        ) {
            moveTo(0f, 24f)
            lineTo(12.039f, 11.961f)
            lineTo(24f, 24f)
            close()
            moveTo(0f, 24f)
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0.118f to Color(0xFF0095D5),
                    0.418f to Color(0xFF3C83DC),
                    0.696f to Color(0xFF6D74E1),
                    0.833f to Color(0xFF806EE3)
                ),
                start = Offset(1.684f, 4.824f),
                end = Offset(8.269f, -1.762f)
            )
        ) {
            moveTo(0f, 0f)
            lineTo(12.039f, 0f)
            lineTo(0f, 13f)
            close()
            moveTo(0f, 0f)
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0.107f to Color(0xFFC757BC),
                    0.214f to Color(0xFFD0609A),
                    0.425f to Color(0xFFE1725C),
                    0.605f to Color(0xFFEE7E2F),
                    0.743f to Color(0xFFF58613),
                    0.823f to Color(0xFFF88909)
                ),
                start = Offset(-4.041f, 22.066f),
                end = Offset(18.293f, -0.268f)
            )
        ) {
            moveTo(12.039f, 0f)
            lineTo(0f, 12.68f)
            lineTo(0f, 24f)
            lineTo(24f, 0f)
            close()
            moveTo(12.039f, 0f)
        }
    }.build()
}
