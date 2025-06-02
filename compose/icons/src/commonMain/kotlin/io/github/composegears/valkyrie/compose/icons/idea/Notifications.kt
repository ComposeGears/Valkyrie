package io.github.composegears.valkyrie.compose.icons.idea

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Idea.Notifications: ImageVector
    get() {
        if (_Notifications != null) {
            return _Notifications!!
        }
        _Notifications = ImageVector.Builder(
            name = "Idea.Notifications",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 0.915741f,
            ) {
                moveTo(6.957f, 14.458f)
                horizontalLineTo(9.049f)
                curveTo(8.993f, 14.587f, 8.912f, 14.706f, 8.811f, 14.808f)
                curveTo(8.596f, 15.022f, 8.306f, 15.142f, 8.003f, 15.142f)
                curveTo(7.7f, 15.142f, 7.41f, 15.022f, 7.195f, 14.808f)
                curveTo(7.094f, 14.706f, 7.013f, 14.587f, 6.957f, 14.458f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
                strokeLineJoin = StrokeJoin.Round,
            ) {
                moveTo(3.947f, 8.224f)
                curveTo(3.982f, 8.154f, 4f, 8.078f, 4f, 8f)
                verticalLineTo(6f)
                curveTo(4f, 4.293f, 4.571f, 3.185f, 5.324f, 2.5f)
                curveTo(6.088f, 1.805f, 7.087f, 1.502f, 8.001f, 1.5f)
                lineTo(8.004f, 1.5f)
                lineTo(8.004f, 1.5f)
                lineTo(8.005f, 1.5f)
                horizontalLineTo(8.005f)
                lineTo(8.006f, 1.5f)
                lineTo(8.009f, 1.5f)
                curveTo(8.923f, 1.502f, 9.919f, 1.805f, 10.68f, 2.5f)
                curveTo(11.431f, 3.184f, 12f, 4.292f, 12f, 6f)
                verticalLineTo(8f)
                curveTo(12f, 8.078f, 12.018f, 8.154f, 12.053f, 8.224f)
                lineTo(13.849f, 11.816f)
                curveTo(14.006f, 12.13f, 13.778f, 12.5f, 13.427f, 12.5f)
                horizontalLineTo(2.573f)
                curveTo(2.222f, 12.5f, 1.994f, 12.13f, 2.151f, 11.816f)
                lineTo(3.947f, 8.224f)
                close()
            }
        }.build()

        return _Notifications!!
    }

@Suppress("ObjectPropertyName")
private var _Notifications: ImageVector? = null
