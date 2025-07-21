package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val FullQualified: ImageVector
    get() {
        if (_FullQualified != null) {
            return _FullQualified!!
        }
        _FullQualified = ImageVector.Builder(
            name = "FullQualified",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            path(
                fill = androidx.compose.ui.graphics.Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF232F34),
                        1f to Color(0xFFD80027)
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
                fill = androidx.compose.ui.graphics.Brush.linearGradient(
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
            path(
                fill = androidx.compose.ui.graphics.Brush.radialGradient(
                    colorStops = arrayOf(
                        0.5f to Color(0xFFFDE030),
                        0.92f to Color(0xFFF7C02B),
                        1f to Color(0xFFF4A223)
                    ),
                    center = Offset(63.6f, 62.9f),
                    radius = 56.96f
                )
            ) {
                moveTo(63.6f, 118.8f)
                curveToRelative(-27.9f, 0f, -58f, -17.5f, -58f, -55.9f)
                reflectiveCurveTo(35.7f, 7f, 63.6f, 7f)
                curveToRelative(15.5f, 0f, 29.8f, 5.1f, 40.4f, 14.4f)
                curveToRelative(11.5f, 10.2f, 17.6f, 24.6f, 17.6f, 41.5f)
                reflectiveCurveToRelative(-6.1f, 31.2f, -17.6f, 41.4f)
                curveTo(93.4f, 113.6f, 79f, 118.8f, 63.6f, 118.8f)
                close()
            }
        }.build()

        return _FullQualified!!
    }

@Suppress("ObjectPropertyName")
private var _FullQualified: ImageVector? = null
