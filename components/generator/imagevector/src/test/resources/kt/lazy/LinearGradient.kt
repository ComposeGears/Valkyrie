package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val LinearGradient: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "LinearGradient",
        defaultWidth = 51.dp,
        defaultHeight = 63.dp,
        viewportWidth = 51f,
        viewportHeight = 63f
    ).apply {
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(0.013f, 44.716f)
            reflectiveCurveToRelative(6.586f, 6.584f, 9.823f, 6.805f)
            curveToRelative(3.236f, 0.224f, 7.033f, 0f, 7.033f, 0f)
            reflectiveCurveToRelative(7.024f, 1.732f, 7.024f, 7.368f)
            verticalLineTo(63f)
            lineToRelative(3.195f, -0.014f)
            verticalLineToRelative(-5.571f)
            curveToRelative(0f, -6.857f, -10.053f, -7.567f, -10.053f, -7.567f)
            reflectiveCurveTo(11.957f, 41.979f, 0.013f, 44.716f)
        }
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(51f, 30.326f)
            reflectiveCurveToRelative(-5.745f, 9.07f, -9.517f, 9.495f)
            curveToRelative(-3.1f, 0.348f, -6.542f, 0.107f, -8.12f, 0.262f)
            curveToRelative(-3.069f, 0.301f, -6.257f, 1.351f, -6.257f, 5.667f)
            verticalLineTo(63f)
            horizontalLineToRelative(-3.182f)
            verticalLineTo(44.546f)
            curveToRelative(0f, -0.964f, 0.006f, -5.235f, 7.093f, -6.584f)
            curveToRelative(1.208f, -0.232f, 3.688f, -0.281f, 4.913f, -0.281f)
            curveToRelative(0.001f, 0f, 4.521f, -7.73f, 15.07f, -7.355f)
        }
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(4.031f, 16.042f)
            reflectiveCurveToRelative(0.669f, 3.435f, 2.899f, 6.315f)
            curveToRelative(2.232f, 2.878f, 4.147f, 4.891f, 6.489f, 4.891f)
            curveToRelative(2.344f, 0f, 6.208f, -0.01f, 7.68f, 0.868f)
            curveToRelative(1.837f, 1.095f, 2.803f, 3.213f, 2.803f, 5.373f)
            verticalLineTo(63f)
            horizontalLineToRelative(3.173f)
            verticalLineTo(33.489f)
            reflectiveCurveToRelative(-0.085f, -3.859f, -3.102f, -6.426f)
            curveToRelative(-1.651f, -1.405f, -2.911f, -2.141f, -5.294f, -2.141f)
            curveToRelative(-0.908f, 0f, -2.041f, -0.019f, -2.041f, -0.019f)
            reflectiveCurveTo(14.853f, 20.75f, 11.45f, 18.7f)
            curveToRelative(-3.404f, -2.049f, -7.419f, -2.658f, -7.419f, -2.658f)
        }
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(39.967f, 0f)
            reflectiveCurveToRelative(0.803f, 7.891f, -2.625f, 11.654f)
            curveToRelative(-3.426f, 3.761f, -5.551f, 2.683f, -7.765f, 3.097f)
            curveToRelative(-1.969f, 0.369f, -2.479f, 1.772f, -2.479f, 3.984f)
            verticalLineToRelative(44.209f)
            horizontalLineToRelative(-3.101f)
            reflectiveCurveToRelative(-0.073f, -43.305f, -0.073f, -44.209f)
            reflectiveCurveToRelative(0.02f, -4.906f, 3.793f, -6.115f)
            curveToRelative(1.592f, -0.509f, 2.335f, -0.376f, 2.917f, -2.293f)
            curveTo(31.218f, 8.408f, 33.04f, 1.99f, 39.967f, 0f)
        }
        path(fill = SolidColor(Color(0xFF000000))) {
            moveTo(11.033f, 0f)
            reflectiveCurveToRelative(-0.802f, 7.891f, 2.625f, 11.654f)
            curveToRelative(3.426f, 3.761f, 5.55f, 2.683f, 7.765f, 3.097f)
            curveToRelative(1.969f, 0.369f, 2.479f, 1.772f, 2.479f, 3.984f)
            verticalLineToRelative(44.209f)
            horizontalLineToRelative(3.101f)
            reflectiveCurveToRelative(0.072f, -43.305f, 0.072f, -44.209f)
            reflectiveCurveToRelative(-0.019f, -4.906f, -3.792f, -6.115f)
            curveToRelative(-1.592f, -0.509f, -2.334f, -0.376f, -2.918f, -2.293f)
            curveTo(19.782f, 8.408f, 17.96f, 1.99f, 11.033f, 0f)
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0.126f to Color(0xFFE7BD76),
                    0.13f to Color(0xFFE6BB74),
                    0.247f to Color(0xFFD48E4E),
                    0.334f to Color(0xFFCC753B),
                    0.38f to Color(0xFFC96C35),
                    0.891f to Color(0xFFC96D34),
                    0.908f to Color(0xFFCC7439),
                    0.937f to Color(0xFFD28647),
                    0.973f to Color(0xFFDEA763),
                    0.989f to Color(0xFFE5B972)
                ),
                start = Offset(46.778f, 40.493f),
                end = Offset(24.105f, 63.166f)
            )
        ) {
            moveTo(51f, 44.716f)
            reflectiveCurveToRelative(-6.586f, 6.584f, -9.823f, 6.805f)
            curveToRelative(-3.235f, 0.224f, -7.032f, 0f, -7.032f, 0f)
            reflectiveCurveToRelative(-7.024f, 1.732f, -7.024f, 7.368f)
            verticalLineTo(63f)
            lineToRelative(-3.195f, -0.014f)
            verticalLineToRelative(-5.571f)
            curveToRelative(0f, -6.857f, 10.052f, -7.567f, 10.052f, -7.567f)
            reflectiveCurveTo(39.057f, 41.979f, 51f, 44.716f)
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0.094f to Color(0xFFE7BD76),
                    0.33f to Color(0xFFC96C35),
                    0.524f to Color(0xFFC96E36),
                    0.614f to Color(0xFFCB7339),
                    0.681f to Color(0xFFCE7C40),
                    0.739f to Color(0xFFD2884A),
                    0.789f to Color(0xFFD99A59),
                    0.834f to Color(0xFFE2B26C),
                    0.844f to Color(0xFFE5B972),
                    1f to Color(0xFFF2D68B)
                ),
                start = Offset(1.627f, 28.701f),
                end = Offset(31.5f, 58.575f)
            )
        ) {
            moveTo(0f, 30.326f)
            reflectiveCurveToRelative(5.744f, 9.07f, 9.516f, 9.495f)
            curveToRelative(3.1f, 0.348f, 6.542f, 0.107f, 8.122f, 0.262f)
            curveToRelative(3.068f, 0.301f, 6.256f, 1.351f, 6.256f, 5.667f)
            verticalLineTo(63f)
            horizontalLineToRelative(3.181f)
            verticalLineTo(44.546f)
            curveToRelative(0f, -0.964f, -0.006f, -5.235f, -7.093f, -6.584f)
            curveToRelative(-1.207f, -0.232f, -3.687f, -0.281f, -4.913f, -0.281f)
            curveToRelative(-0.001f, 0f, -4.522f, -7.73f, -15.069f, -7.355f)
        }
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0.094f to Color(0xFFE7BD76),
                    0.226f to Color(0xFFC96C35),
                    0.376f to Color(0xFFC96E36),
                    0.491f to Color(0xFFCC753B),
                    0.596f to Color(0xFFD08143),
                    0.694f to Color(0xFFD59151),
                    0.786f to Color(0xFFDEA965),
                    0.834f to Color(0xFFE5B972),
                    1f to Color(0xFFF2D68B)
                ),
                start = Offset(44.384f, 14.55f),
                end = Offset(18.29f, 59.746f)
            )
        ) {
            moveTo(46.969f, 16.042f)
            reflectiveCurveToRelative(-0.669f, 3.435f, -2.898f, 6.315f)
            curveToRelative(-2.232f, 2.878f, -4.147f, 4.891f, -6.489f, 4.891f)
            curveToRelative(-2.344f, 0f, -6.208f, -0.01f, -7.68f, 0.868f)
            curveToRelative(-1.837f, 1.095f, -2.803f, 3.213f, -2.803f, 5.373f)
            verticalLineTo(63f)
            horizontalLineToRelative(-3.174f)
            verticalLineTo(33.489f)
            reflectiveCurveToRelative(0.086f, -3.859f, 3.103f, -6.426f)
            curveToRelative(1.651f, -1.405f, 2.911f, -2.141f, 5.295f, -2.141f)
            curveToRelative(0.907f, 0f, 2.041f, -0.019f, 2.041f, -0.019f)
            reflectiveCurveToRelative(1.785f, -4.153f, 5.187f, -6.203f)
            curveToRelative(3.403f, -2.049f, 7.418f, -2.658f, 7.418f, -2.658f)
        }
    }.build()
}
