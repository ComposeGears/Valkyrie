package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Help: ImageVector
    get() {
        if (_Help != null) {
            return _Help!!
        }
        _Help = ImageVector.Builder(
            name = "Help",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
            autoMirror = true,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                strokeLineWidth = 1f,
                strokeLineJoin = StrokeJoin.Bevel,
                strokeLineMiter = 1f,
            ) {
                moveTo(12.0f, 2.0f)
                curveTo(6.48f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
                reflectiveCurveToRelative(4.48f, 10.0f, 10.0f, 10.0f)
                reflectiveCurveToRelative(10.0f, -4.48f, 10.0f, -10.0f)
                reflectiveCurveTo(17.52f, 2.0f, 12.0f, 2.0f)
                close()
                moveTo(13.0f, 19.0f)
                horizontalLineToRelative(-2.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineToRelative(2.0f)
                verticalLineToRelative(2.0f)
                close()
                moveTo(15.07f, 11.25f)
                lineToRelative(-0.9f, 0.92f)
                curveTo(13.45f, 12.9f, 13.0f, 13.5f, 13.0f, 15.0f)
                horizontalLineToRelative(-2.0f)
                verticalLineToRelative(-0.5f)
                curveToRelative(0.0f, -1.1f, 0.45f, -2.1f, 1.17f, -2.83f)
                lineToRelative(1.24f, -1.26f)
                curveToRelative(0.37f, -0.36f, 0.59f, -0.86f, 0.59f, -1.41f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                reflectiveCurveToRelative(-2.0f, 0.9f, -2.0f, 2.0f)
                lineTo(8.0f, 9.0f)
                curveToRelative(0.0f, -2.21f, 1.79f, -4.0f, 4.0f, -4.0f)
                reflectiveCurveToRelative(4.0f, 1.79f, 4.0f, 4.0f)
                curveToRelative(0.0f, 0.88f, -0.36f, 1.68f, -0.93f, 2.25f)
                close()
            }
        }.build()

        return _Help!!
    }

@Suppress("ObjectPropertyName")
private var _Help: ImageVector? = null
