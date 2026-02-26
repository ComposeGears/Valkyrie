package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Colored.BoxLogo: ImageVector
    get() {
        if (_BoxLogo != null) return _BoxLogo!!
        _BoxLogo = ImageVector.Builder(
            name = "Boxicons",
            defaultWidth = 32.dp,
            defaultHeight = 32.dp,
            viewportWidth = 32f,
            viewportHeight = 32f,
        ).apply {
            // White circle background
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(16f, 6f)
                arcToRelative(10f, 10f, 0f, true, true, 0f, 20f)
                arcToRelative(10f, 10f, 0f, true, true, 0f, -20f)
                close()
            }
            // Gradient fill clipped to logo shape (replacement for foreignObject conic-gradient)
            group(
                clipPathData = PathData {
                    moveTo(26.1261f, 6.88263f)
                    lineTo(18.9116f, 2.76173f)
                    curveTo(17.1255f, 1.74609f, 14.9308f, 1.74609f, 13.1447f, 2.76173f)
                    lineTo(5.93016f, 6.88263f)
                    curveTo(4.1207f, 7.92162f, 3f, 9.84782f, 3f, 11.9258f)
                    verticalLineTo(20.0742f)
                    curveTo(3f, 22.1639f, 4.1207f, 24.0901f, 5.93016f, 25.1174f)
                    lineTo(13.1447f, 29.2383f)
                    curveTo(14.9308f, 30.2539f, 17.1255f, 30.2539f, 18.9116f, 29.2383f)
                    lineTo(26.1261f, 25.1174f)
                    curveTo(27.9356f, 24.0784f, 29.0563f, 22.1522f, 29.0563f, 20.0742f)
                    verticalLineTo(11.9258f)
                    curveTo(29.0563f, 9.83615f, 27.9356f, 7.90994f, 26.1261f, 6.88263f)
                    close()
                    moveTo(22.4838f, 13.35f)
                    lineTo(17.429f, 16.1634f)
                    curveTo(17.1138f, 16.3385f, 16.9037f, 16.6771f, 16.9037f, 17.0507f)
                    verticalLineTo(22.6308f)
                    curveTo(16.9037f, 23.8099f, 15.6196f, 24.5337f, 14.6156f, 23.9383f)
                    lineTo(9.68918f, 21.0081f)
                    curveTo(9.2339f, 20.7396f, 8.94205f, 20.2376f, 8.94205f, 19.7006f)
                    verticalLineTo(12.9181f)
                    curveTo(8.94205f, 12.3694f, 9.24557f, 11.8557f, 9.7242f, 11.5872f)
                    lineTo(16.1682f, 8.00334f)
                    curveTo(16.6235f, 7.74651f, 17.1839f, 7.74651f, 17.6391f, 8.00334f)
                    lineTo(22.4722f, 10.6883f)
                    curveTo(23.5112f, 11.2604f, 23.5112f, 12.7663f, 22.4722f, 13.3383f)
                    lineTo(22.4838f, 13.35f)
                    close()
                },
            ) {
                path(
                    fill = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFF209BF6), // blue
                            Color(0xFF12DA6C), // green
                            Color(0xFFE1FE42), // yellow
                            Color(0xFFED7EFF), // pink
                            Color(0xFF902FFB), // violet
                            Color(0xFF209BF6), // repeat first for smooth wrap
                        ),
                        center = Offset(16f, 16f),
                    ),
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(-3f, -4f)
                    lineTo(32f, -4f)
                    lineTo(32f, 34f)
                    lineTo(-3f, 34f)
                    close()
                }
            }
        }.build()
        return _BoxLogo!!
    }

@Suppress("ObjectPropertyName")
private var _BoxLogo: ImageVector? = null
