package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ComposeColorLinearGradient: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "ComposeColorLinearGradient",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 18f,
        viewportHeight = 18f
    ).apply {
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Black,
                    1f to Color.DarkGray
                ),
                start = Offset(1f, 1f),
                end = Offset(5f, 1f)
            )
        ) {
            moveTo(1f, 1f)
            lineTo(5f, 1f)
            lineTo(5f, 5f)
            lineTo(1f, 5f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.DarkGray,
                    1f to Color.Gray
                ),
                start = Offset(6f, 1f),
                end = Offset(10f, 1f)
            )
        ) {
            moveTo(6f, 1f)
            lineTo(10f, 1f)
            lineTo(10f, 5f)
            lineTo(6f, 5f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Gray,
                    1f to Color.LightGray
                ),
                start = Offset(11f, 1f),
                end = Offset(15f, 1f)
            )
        ) {
            moveTo(11f, 1f)
            lineTo(15f, 1f)
            lineTo(15f, 5f)
            lineTo(11f, 5f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.LightGray,
                    1f to Color.White
                ),
                start = Offset(1f, 6f),
                end = Offset(5f, 6f)
            )
        ) {
            moveTo(1f, 6f)
            lineTo(5f, 6f)
            lineTo(5f, 10f)
            lineTo(1f, 10f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.White,
                    1f to Color.Red
                ),
                start = Offset(6f, 6f),
                end = Offset(10f, 6f)
            )
        ) {
            moveTo(6f, 6f)
            lineTo(10f, 6f)
            lineTo(10f, 10f)
            lineTo(6f, 10f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Red,
                    1f to Color.Green
                ),
                start = Offset(11f, 6f),
                end = Offset(15f, 6f)
            )
        ) {
            moveTo(11f, 6f)
            lineTo(15f, 6f)
            lineTo(15f, 10f)
            lineTo(11f, 10f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Green,
                    1f to Color.Blue
                ),
                start = Offset(1f, 11f),
                end = Offset(5f, 11f)
            )
        ) {
            moveTo(1f, 11f)
            lineTo(5f, 11f)
            lineTo(5f, 15f)
            lineTo(1f, 15f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Blue,
                    1f to Color.Yellow
                ),
                start = Offset(6f, 11f),
                end = Offset(10f, 11f)
            )
        ) {
            moveTo(6f, 11f)
            lineTo(10f, 11f)
            lineTo(10f, 15f)
            lineTo(6f, 15f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Yellow,
                    1f to Color.Cyan
                ),
                start = Offset(11f, 11f),
                end = Offset(15f, 11f)
            )
        ) {
            moveTo(11f, 11f)
            lineTo(15f, 11f)
            lineTo(15f, 15f)
            lineTo(11f, 15f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Cyan,
                    1f to Color.Magenta
                ),
                start = Offset(4.5f, 4.5f),
                end = Offset(8.5f, 4.5f)
            )
        ) {
            moveTo(4.5f, 4.5f)
            lineTo(8.5f, 4.5f)
            lineTo(8.5f, 8.5f)
            lineTo(4.5f, 8.5f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Magenta,
                    1f to Color.Red.copy(alpha = 0.5019608f)
                ),
                start = Offset(9.5f, 9.5f),
                end = Offset(12.5f, 9.5f)
            )
        ) {
            moveTo(9.5f, 9.5f)
            lineTo(12.5f, 9.5f)
            lineTo(12.5f, 12.5f)
            lineTo(9.5f, 12.5f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Red.copy(alpha = 0.5019608f),
                    1f to Color.Green.copy(alpha = 0.5019608f)
                ),
                start = Offset(1f, 8.5f),
                end = Offset(4.5f, 8.5f)
            )
        ) {
            moveTo(1f, 8.5f)
            lineTo(4.5f, 8.5f)
            lineTo(4.5f, 12f)
            lineTo(1f, 12f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Green.copy(alpha = 0.5019608f),
                    1f to Color.Blue.copy(alpha = 0.5019608f)
                ),
                start = Offset(5f, 8.5f),
                end = Offset(8.5f, 8.5f)
            )
        ) {
            moveTo(5f, 8.5f)
            lineTo(8.5f, 8.5f)
            lineTo(8.5f, 12f)
            lineTo(5f, 12f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Blue.copy(alpha = 0.5019608f),
                    1f to Color.Black.copy(alpha = 0.2509804f)
                ),
                start = Offset(9f, 8.5f),
                end = Offset(12.5f, 8.5f)
            )
        ) {
            moveTo(9f, 8.5f)
            lineTo(12.5f, 8.5f)
            lineTo(12.5f, 12f)
            lineTo(9f, 12f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Black.copy(alpha = 0.2509804f),
                    1f to Color.White.copy(alpha = 0.7490196f)
                ),
                start = Offset(2f, 2f),
                end = Offset(6f, 2f)
            )
        ) {
            moveTo(2f, 2f)
            lineTo(6f, 2f)
            lineTo(6f, 6f)
            lineTo(2f, 6f)
            close()
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.White.copy(alpha = 0.7490196f),
                    1f to Color.Black
                ),
                start = Offset(6f, 2f),
                end = Offset(10f, 2f)
            )
        ) {
            moveTo(6f, 2f)
            lineTo(10f, 2f)
            lineTo(10f, 6f)
            lineTo(6f, 6f)
            close()
        }
    }.build()
}
