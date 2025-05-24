package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.ActualZoom: ImageVector
    get() {
        if (_ActualZoom != null) {
            return _ActualZoom!!
        }
        _ActualZoom = ImageVector.Builder(
            name = "ActualZoom",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(fill = SolidColor(Color(0xFF6C707E))) {
                moveTo(3.346f, 12f)
                verticalLineTo(6.096f)
                horizontalLineTo(1.372f)
                verticalLineTo(4.872f)
                horizontalLineTo(2.488f)
                curveTo(2.732f, 4.872f, 2.946f, 4.82f, 3.13f, 4.716f)
                curveTo(3.318f, 4.612f, 3.462f, 4.464f, 3.562f, 4.272f)
                curveTo(3.662f, 4.08f, 3.712f, 3.856f, 3.712f, 3.6f)
                horizontalLineTo(3.718f)
                horizontalLineTo(4.816f)
                verticalLineTo(12f)
                horizontalLineTo(3.346f)
                close()
                moveTo(7.069f, 10.398f)
                horizontalLineTo(8.671f)
                verticalLineTo(12f)
                horizontalLineTo(7.069f)
                verticalLineTo(10.398f)
                close()
                moveTo(7.069f, 5.658f)
                horizontalLineTo(8.671f)
                verticalLineTo(7.26f)
                horizontalLineTo(7.069f)
                verticalLineTo(5.658f)
                close()
                moveTo(11.962f, 12f)
                verticalLineTo(6.096f)
                horizontalLineTo(9.988f)
                verticalLineTo(4.872f)
                horizontalLineTo(11.104f)
                curveTo(11.348f, 4.872f, 11.562f, 4.82f, 11.746f, 4.716f)
                curveTo(11.934f, 4.612f, 12.078f, 4.464f, 12.178f, 4.272f)
                curveTo(12.278f, 4.08f, 12.328f, 3.856f, 12.328f, 3.6f)
                horizontalLineTo(12.334f)
                horizontalLineTo(13.432f)
                verticalLineTo(12f)
                horizontalLineTo(11.962f)
                close()
            }
        }.build()

        return _ActualZoom!!
    }

@Suppress("ObjectPropertyName")
private var _ActualZoom: ImageVector? = null
