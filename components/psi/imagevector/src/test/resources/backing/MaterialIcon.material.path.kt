package io.github.composegears.valkyrie

import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _notificationsAlert: ImageVector? = null

val Icons.NotificationsAlert: ImageVector
    get() {
        if (_notificationsAlert != null) {
            return _notificationsAlert!!
        }
        _notificationsAlert = Builder(
            name = "NotificationsAlert",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f,
        ).apply {
            materialPath {
                moveTo(18.75f, 16.5385f)
                verticalLineTo(11.1538f)
                curveTo(18.75f, 7.8477f, 16.905f, 5.08f, 13.6875f, 4.3477f)
                verticalLineTo(3.6154f)
                curveTo(13.6875f, 2.7215f, 12.9338f, 2.0f, 12.0f, 2.0f)
                curveTo(11.0662f, 2.0f, 10.3125f, 2.7215f, 10.3125f, 3.6154f)
                verticalLineTo(4.3477f)
                curveTo(7.0837f, 5.08f, 5.25f, 7.8369f, 5.25f, 11.1538f)
                verticalLineTo(16.5385f)
                lineTo(3.0f, 18.6923f)
                verticalLineTo(19.7692f)
                horizontalLineTo(21.0f)
                verticalLineTo(18.6923f)
                lineTo(18.75f, 16.5385f)
                close()
                moveTo(13.125f, 16.5385f)
                horizontalLineTo(10.875f)
                verticalLineTo(14.3846f)
                horizontalLineTo(13.125f)
                verticalLineTo(16.5385f)
                close()
                moveTo(13.125f, 12.2308f)
                horizontalLineTo(10.875f)
                verticalLineTo(7.9231f)
                horizontalLineTo(13.125f)
                verticalLineTo(12.2308f)
                close()
                moveTo(12.0f, 23.0f)
                curveTo(13.2375f, 23.0f, 14.25f, 22.0308f, 14.25f, 20.8462f)
                horizontalLineTo(9.75f)
                curveTo(9.75f, 22.0308f, 10.7512f, 23.0f, 12.0f, 23.0f)
                close()
            }
        }.build()
        return _notificationsAlert!!
    }
