package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.AddFile: ImageVector
    get() {
        if (_AddFile != null) {
            return _AddFile!!
        }
        _AddFile = ImageVector.Builder(
            name = "AddFile",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                fill = SolidColor(Color(0xFF3574F0)),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(12.5f, 9f)
                curveTo(12.776f, 9f, 13f, 9.224f, 13f, 9.5f)
                verticalLineTo(12f)
                horizontalLineTo(15.5f)
                curveTo(15.776f, 12f, 16f, 12.224f, 16f, 12.5f)
                curveTo(16f, 12.776f, 15.776f, 13f, 15.5f, 13f)
                horizontalLineTo(13f)
                verticalLineTo(15.5f)
                curveTo(13f, 15.776f, 12.776f, 16f, 12.5f, 16f)
                curveTo(12.224f, 16f, 12f, 15.776f, 12f, 15.5f)
                verticalLineTo(13f)
                horizontalLineTo(9.5f)
                curveTo(9.224f, 13f, 9f, 12.776f, 9f, 12.5f)
                curveTo(9f, 12.224f, 9.224f, 12f, 9.5f, 12f)
                horizontalLineTo(12f)
                verticalLineTo(9.5f)
                curveTo(12f, 9.224f, 12.224f, 9f, 12.5f, 9f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(3f, 13f)
                verticalLineTo(5.828f)
                curveTo(3f, 5.298f, 3.211f, 4.789f, 3.586f, 4.414f)
                lineTo(6.414f, 1.586f)
                curveTo(6.789f, 1.211f, 7.298f, 1f, 7.828f, 1f)
                horizontalLineTo(11f)
                curveTo(12.105f, 1f, 13f, 1.895f, 13f, 3f)
                verticalLineTo(8f)
                horizontalLineTo(12f)
                verticalLineTo(3f)
                curveTo(12f, 2.448f, 11.552f, 2f, 11f, 2f)
                horizontalLineTo(8f)
                verticalLineTo(4f)
                curveTo(8f, 5.105f, 7.105f, 6f, 6f, 6f)
                horizontalLineTo(4f)
                verticalLineTo(13f)
                curveTo(4f, 13.552f, 4.448f, 14f, 5f, 14f)
                horizontalLineTo(8f)
                verticalLineTo(15f)
                horizontalLineTo(5f)
                curveTo(3.895f, 15f, 3f, 14.105f, 3f, 13f)
                close()
                moveTo(4.414f, 5f)
                lineTo(7f, 2.414f)
                verticalLineTo(4f)
                curveTo(7f, 4.552f, 6.552f, 5f, 6f, 5f)
                horizontalLineTo(4.414f)
                close()
            }
        }.build()

        return _AddFile!!
    }

@Suppress("ObjectPropertyName")
private var _AddFile: ImageVector? = null
