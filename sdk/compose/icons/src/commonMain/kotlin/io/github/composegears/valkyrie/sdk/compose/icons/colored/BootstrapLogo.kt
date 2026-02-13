package io.github.composegears.valkyrie.sdk.compose.icons.colored

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons

val ValkyrieIcons.Colored.BootstrapLogo: ImageVector
    get() {
        if (_BootstrapLogo != null) {
            return _BootstrapLogo!!
        }
        _BootstrapLogo = ImageVector.Builder(
            name = "BootstrapLogo",
            defaultWidth = 512.dp,
            defaultHeight = 408.dp,
            viewportWidth = 512f,
            viewportHeight = 408f,
        ).apply {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF9013FE),
                        1f to Color(0xFF6610F2),
                    ),
                    start = Offset(76.08f, 10.8f),
                    end = Offset(523.48f, 365.94f),
                ),
            ) {
                moveTo(56.48f, 53.32f)
                curveTo(55.51f, 25.58f, 77.13f, 0f, 106.34f, 0f)
                horizontalLineToRelative(299.35f)
                curveToRelative(29.21f, 0f, 50.83f, 25.58f, 49.86f, 53.32f)
                curveToRelative(-0.93f, 26.65f, 0.28f, 61.17f, 8.96f, 89.31f)
                curveToRelative(8.72f, 28.23f, 23.41f, 46.08f, 47.48f, 48.37f)
                verticalLineToRelative(26f)
                curveToRelative(-24.07f, 2.29f, -38.76f, 20.14f, -47.48f, 48.37f)
                curveToRelative(-8.69f, 28.15f, -9.89f, 62.66f, -8.96f, 89.31f)
                curveToRelative(0.97f, 27.74f, -20.65f, 53.32f, -49.86f, 53.32f)
                horizontalLineTo(106.34f)
                curveToRelative(-29.21f, 0f, -50.83f, -25.58f, -49.86f, -53.32f)
                curveToRelative(0.93f, -26.65f, -0.28f, -61.17f, -8.97f, -89.31f)
                curveTo(38.8f, 237.14f, 24.07f, 219.29f, 0f, 217f)
                verticalLineToRelative(-26f)
                curveToRelative(24.07f, -2.29f, 38.8f, -20.14f, 47.52f, -48.37f)
                curveToRelative(8.69f, -28.15f, 9.89f, -62.66f, 8.97f, -89.31f)
                close()
            }
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color.White,
                        1f to Color(0xFFF1E5FC),
                    ),
                    start = Offset(193.51f, 109.74f),
                    end = Offset(293.51f, 278.87f),
                ),
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1f,
            ) {
                moveTo(267.1f, 312.46f)
                curveToRelative(47.3f, 0f, 75.8f, -23.16f, 75.8f, -61.35f)
                curveToRelative(0f, -28.87f, -20.34f, -49.78f, -50.53f, -53.08f)
                verticalLineToRelative(-1.2f)
                curveToRelative(22.18f, -3.61f, 39.59f, -24.21f, 39.59f, -47.22f)
                curveToRelative(0f, -32.78f, -25.88f, -54.14f, -65.32f, -54.14f)
                horizontalLineToRelative(-88.74f)
                verticalLineToRelative(217f)
                horizontalLineToRelative(89.2f)
                close()
                moveTo(212.41f, 122.98f)
                horizontalLineToRelative(45.91f)
                curveToRelative(24.96f, 0f, 39.13f, 11.13f, 39.13f, 31.28f)
                curveToRelative(0f, 21.5f, -16.48f, 33.53f, -46.37f, 33.53f)
                horizontalLineToRelative(-38.67f)
                verticalLineToRelative(-64.81f)
                close()
                moveTo(212.41f, 284.94f)
                verticalLineToRelative(-71.43f)
                horizontalLineToRelative(45.6f)
                curveToRelative(32.66f, 0f, 49.61f, 12.03f, 49.61f, 35.49f)
                curveToRelative(0f, 23.46f, -16.48f, 35.94f, -47.6f, 35.94f)
                horizontalLineToRelative(-47.6f)
                close()
            }
        }.build()

        return _BootstrapLogo!!
    }

@Suppress("ObjectPropertyName")
private var _BootstrapLogo: ImageVector? = null
