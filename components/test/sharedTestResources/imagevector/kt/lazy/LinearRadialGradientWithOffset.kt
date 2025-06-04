package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val LinearRadialGradientWithOffset: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "LinearRadialGradientWithOffset",
        defaultWidth = 128.dp,
        defaultHeight = 128.dp,
        viewportWidth = 128f,
        viewportHeight = 128f
    ).apply {
        path(
            fill = Brush.radialGradient(
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
        path(fill = SolidColor(Color(0xFFEB8F00))) {
            moveTo(111.49f, 29.67f)
            curveToRelative(5.33f, 8.6f, 8.11f, 18.84f, 8.11f, 30.23f)
            curveToRelative(0f, 16.9f, -6.1f, 31.2f, -17.6f, 41.4f)
            curveToRelative(-10.6f, 9.3f, -25f, 14.5f, -40.4f, 14.5f)
            curveToRelative(-18.06f, 0f, -37f, -7.35f, -48.18f, -22.94f)
            curveToRelative(10.76f, 17.66f, 31f, 25.94f, 50.18f, 25.94f)
            curveToRelative(15.4f, 0f, 29.8f, -5.2f, 40.4f, -14.5f)
            curveToRelative(11.5f, -10.2f, 17.6f, -24.5f, 17.6f, -41.4f)
            curveTo(121.6f, 50.16f, 118.13f, 38.84f, 111.49f, 29.67f)
            close()
        }
        group(
            clipPathData = PathData {
                moveTo(66.8f, 76.5f)
                curveToRelative(-10.89f, 3.76f, -22.1f, 6.51f, -33.5f, 8.2f)
                curveToRelative(-1.92f, 0.25f, -3.27f, 2.02f, -3.02f, 3.94f)
                curveToRelative(0.06f, 0.44f, 0.2f, 0.87f, 0.42f, 1.26f)
                curveToRelative(8.2f, 14.2f, 27.4f, 21.6f, 45.8f, 15.4f)
                curveToRelative(20.2f, -6.8f, 29.4f, -24.2f, 27.2f, -40.1f)
                curveToRelative(-0.27f, -1.9f, -2.03f, -3.22f, -3.92f, -2.95f)
                curveToRelative(-0.45f, 0.06f, -0.88f, 0.22f, -1.28f, 0.45f)
                curveTo(88.36f, 68.22f, 77.75f, 72.83f, 66.8f, 76.5f)
                close()
            }
        ) {
            path(
                fill = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF404040),
                        0.12f to Color(0xFF3E3A35),
                        0.44f to Color(0xFF392D1E),
                        0.74f to Color(0xFF362510),
                        1f to Color(0xFF35220B)
                    ),
                    start = Offset(30.25f, 84.86f),
                    end = Offset(104.02f, 84.86f)
                )
            ) {
                moveTo(66.8f, 76.5f)
                curveToRelative(-10.89f, 3.76f, -22.1f, 6.51f, -33.5f, 8.2f)
                curveToRelative(-1.92f, 0.25f, -3.27f, 2.02f, -3.02f, 3.94f)
                curveToRelative(0.06f, 0.44f, 0.2f, 0.87f, 0.42f, 1.26f)
                curveToRelative(8.2f, 14.2f, 27.4f, 21.6f, 45.8f, 15.4f)
                curveToRelative(20.2f, -6.8f, 29.4f, -24.2f, 27.2f, -40.1f)
                curveToRelative(-0.27f, -1.9f, -2.03f, -3.22f, -3.92f, -2.95f)
                curveToRelative(-0.45f, 0.06f, -0.88f, 0.22f, -1.28f, 0.45f)
                curveTo(88.36f, 68.22f, 77.75f, 72.83f, 66.8f, 76.5f)
                close()
            }
        }
        path(fill = SolidColor(Color.White)) {
            moveTo(80.4f, 22.9f)
            curveToRelative(9.94f, 0.09f, 17.93f, 8.22f, 17.84f, 18.16f)
            curveToRelative(-0.06f, 7.19f, -4.41f, 13.66f, -11.04f, 16.44f)
            curveToRelative(-2.16f, 0.88f, -4.47f, 1.32f, -6.8f, 1.3f)
            curveToRelative(-9.94f, -0.09f, -17.93f, -8.22f, -17.84f, -18.16f)
            curveToRelative(0.06f, -7.19f, 4.41f, -13.66f, 11.04f, -16.44f)
            curveTo(75.76f, 23.34f, 78.07f, 22.9f, 80.4f, 22.9f)
        }
        path(fill = SolidColor(Color(0xFF422B0D))) {
            moveTo(80.4f, 41f)
            moveToRelative(-7f, 0f)
            arcToRelative(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, 14f, 0f)
            arcToRelative(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, -14f, 0f)
        }
        path(fill = SolidColor(Color.White)) {
            moveTo(35.9f, 44.78f)
            curveToRelative(-6.69f, -0.01f, -12.12f, 5.41f, -12.13f, 12.09f)
            curveTo(23.76f, 63.56f, 29.18f, 68.99f, 35.87f, 69f)
            curveToRelative(6.69f, 0.01f, 12.12f, -5.41f, 12.13f, -12.09f)
            curveToRelative(0f, -1.56f, -0.3f, -3.11f, -0.88f, -4.56f)
            curveTo(45.26f, 47.78f, 40.83f, 44.79f, 35.9f, 44.78f)
            close()
        }
        path(fill = SolidColor(Color(0xFF422B0D))) {
            moveTo(36f, 57f)
            moveToRelative(-7f, 0f)
            arcToRelative(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, 14f, 0f)
            arcToRelative(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, -14f, 0f)
        }
    }.build()
}
