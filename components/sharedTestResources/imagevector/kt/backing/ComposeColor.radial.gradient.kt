package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ComposeColorRadialGradient: ImageVector
    get() {
        if (_ComposeColorRadialGradient != null) {
            return _ComposeColorRadialGradient!!
        }
        _ComposeColorRadialGradient = ImageVector.Builder(
            name = "ComposeColorRadialGradient",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Black,
                        1f to Color.DarkGray
                    ),
                    center = Offset(3f, 3f),
                    radius = 2.5f
                )
            ) {
                moveTo(1f, 1f)
                lineTo(5f, 1f)
                lineTo(5f, 5f)
                lineTo(1f, 5f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.DarkGray,
                        1f to Color.Gray
                    ),
                    center = Offset(8f, 3f),
                    radius = 2.5f
                )
            ) {
                moveTo(6f, 1f)
                lineTo(10f, 1f)
                lineTo(10f, 5f)
                lineTo(6f, 5f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Gray,
                        1f to Color.LightGray
                    ),
                    center = Offset(13f, 3f),
                    radius = 2.5f
                )
            ) {
                moveTo(11f, 1f)
                lineTo(15f, 1f)
                lineTo(15f, 5f)
                lineTo(11f, 5f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.LightGray,
                        1f to Color.White
                    ),
                    center = Offset(3f, 8f),
                    radius = 2.5f
                )
            ) {
                moveTo(1f, 6f)
                lineTo(5f, 6f)
                lineTo(5f, 10f)
                lineTo(1f, 10f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.White,
                        1f to Color.Red
                    ),
                    center = Offset(8f, 8f),
                    radius = 2.5f
                )
            ) {
                moveTo(6f, 6f)
                lineTo(10f, 6f)
                lineTo(10f, 10f)
                lineTo(6f, 10f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Red,
                        1f to Color.Green
                    ),
                    center = Offset(13f, 8f),
                    radius = 2.5f
                )
            ) {
                moveTo(11f, 6f)
                lineTo(15f, 6f)
                lineTo(15f, 10f)
                lineTo(11f, 10f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Green,
                        1f to Color.Blue
                    ),
                    center = Offset(3f, 13f),
                    radius = 2.5f
                )
            ) {
                moveTo(1f, 11f)
                lineTo(5f, 11f)
                lineTo(5f, 15f)
                lineTo(1f, 15f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Blue,
                        1f to Color.Yellow
                    ),
                    center = Offset(8f, 13f),
                    radius = 2.5f
                )
            ) {
                moveTo(6f, 11f)
                lineTo(10f, 11f)
                lineTo(10f, 15f)
                lineTo(6f, 15f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Yellow,
                        1f to Color.Cyan
                    ),
                    center = Offset(13f, 13f),
                    radius = 2.5f
                )
            ) {
                moveTo(11f, 11f)
                lineTo(15f, 11f)
                lineTo(15f, 15f)
                lineTo(11f, 15f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Cyan,
                        1f to Color.Magenta
                    ),
                    center = Offset(6.5f, 6.5f),
                    radius = 2f
                )
            ) {
                moveTo(4.5f, 4.5f)
                lineTo(8.5f, 4.5f)
                lineTo(8.5f, 8.5f)
                lineTo(4.5f, 8.5f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Magenta,
                        1f to Color.Red.copy(alpha = 0.5019608f)
                    ),
                    center = Offset(11f, 11f),
                    radius = 1.5f
                )
            ) {
                moveTo(9.5f, 9.5f)
                lineTo(12.5f, 9.5f)
                lineTo(12.5f, 12.5f)
                lineTo(9.5f, 12.5f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Red.copy(alpha = 0.5019608f),
                        1f to Color.Green.copy(alpha = 0.5019608f)
                    ),
                    center = Offset(2.75f, 10.25f),
                    radius = 1.75f
                )
            ) {
                moveTo(1f, 8.5f)
                lineTo(4.5f, 8.5f)
                lineTo(4.5f, 12f)
                lineTo(1f, 12f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Green.copy(alpha = 0.5019608f),
                        1f to Color.Blue.copy(alpha = 0.5019608f)
                    ),
                    center = Offset(6.75f, 10.25f),
                    radius = 1.75f
                )
            ) {
                moveTo(5f, 8.5f)
                lineTo(8.5f, 8.5f)
                lineTo(8.5f, 12f)
                lineTo(5f, 12f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Blue.copy(alpha = 0.5019608f),
                        1f to Color.Black.copy(alpha = 0.2509804f)
                    ),
                    center = Offset(10.75f, 10.25f),
                    radius = 1.75f
                )
            ) {
                moveTo(9f, 8.5f)
                lineTo(12.5f, 8.5f)
                lineTo(12.5f, 12f)
                lineTo(9f, 12f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Black.copy(alpha = 0.2509804f),
                        1f to Color.White.copy(alpha = 0.7490196f)
                    ),
                    center = Offset(4f, 4f),
                    radius = 2f
                )
            ) {
                moveTo(2f, 2f)
                lineTo(6f, 2f)
                lineTo(6f, 6f)
                lineTo(2f, 6f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.White.copy(alpha = 0.7490196f),
                        1f to Color.Black
                    ),
                    center = Offset(8f, 4f),
                    radius = 2f
                )
            ) {
                moveTo(6f, 2f)
                lineTo(10f, 2f)
                lineTo(10f, 6f)
                lineTo(6f, 6f)
                close()
            }
        }.build()

        return _ComposeColorRadialGradient!!
    }

@Suppress("ObjectPropertyName")
private var _ComposeColorRadialGradient: ImageVector? = null
