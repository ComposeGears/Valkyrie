import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconWithGradient: ImageVector
    get() {
        if (_IconWithGradient != null) {
            return _IconWithGradient!!
        }
        _IconWithGradient = ImageVector.Builder(
            name = "IconWithGradient",
            defaultWidth = 51.dp,
            defaultHeight = 63.dp,
            viewportWidth = 51f,
            viewportHeight = 63f,
        ).apply {
            path(
                fill = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.19f to Color(0xFFD53A42),
                        0.39f to Color(0xFFDF7A40),
                        0.59f to Color(0xFFF0A941),
                        1f to Color(0xFFFFFFF0),
                    ),
                    center = Offset(0f, 10f),
                    radius = 100f,
                ),
                stroke = SolidColor(Color(0x00000000)),
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(100f)
                verticalLineToRelative(20f)
                horizontalLineToRelative(-100f)
                close()
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
                        0.989f to Color(0xFFE5B972),
                    ),
                    start = Offset(46.778f, 40.493f),
                    end = Offset(24.105f, 63.166f),
                ),
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
        }.build()

        return _IconWithGradient!!
    }

@Suppress("ObjectPropertyName")
private var _IconWithGradient: ImageVector? = null
