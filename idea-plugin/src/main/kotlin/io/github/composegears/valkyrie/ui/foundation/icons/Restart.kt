package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.Restart: ImageVector
    get() {
        if (_Restart != null) {
            return _Restart!!
        }
        _Restart = ImageVector.Builder(
            name = "Restart",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                fill = SolidColor(Color(0xFF6C707E)),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(13.55f, 1.4f)
                curveTo(13.55f, 1.124f, 13.326f, 0.9f, 13.05f, 0.9f)
                curveTo(12.774f, 0.9f, 12.55f, 1.124f, 12.55f, 1.4f)
                verticalLineTo(4.089f)
                curveTo(11.45f, 2.81f, 9.82f, 2f, 8f, 2f)
                curveTo(4.686f, 2f, 2f, 4.686f, 2f, 8f)
                curveTo(2f, 11.314f, 4.686f, 14f, 8f, 14f)
                curveTo(10.392f, 14f, 12.456f, 12.6f, 13.419f, 10.578f)
                curveTo(13.538f, 10.328f, 13.432f, 10.03f, 13.183f, 9.911f)
                curveTo(12.934f, 9.792f, 12.635f, 9.898f, 12.517f, 10.148f)
                curveTo(11.713f, 11.835f, 9.992f, 13f, 8f, 13f)
                curveTo(5.239f, 13f, 3f, 10.761f, 3f, 8f)
                curveTo(3f, 5.238f, 5.239f, 3f, 8f, 3f)
                curveTo(9.59f, 3f, 11.007f, 3.742f, 11.923f, 4.9f)
                horizontalLineTo(9.05f)
                curveTo(8.774f, 4.9f, 8.55f, 5.124f, 8.55f, 5.4f)
                curveTo(8.55f, 5.676f, 8.774f, 5.9f, 9.05f, 5.9f)
                horizontalLineTo(13.05f)
                horizontalLineTo(13.55f)
                verticalLineTo(5.4f)
                verticalLineTo(1.4f)
                close()
                moveTo(12.521f, 4.9f)
                horizontalLineTo(12.55f)
                verticalLineTo(4.88f)
                lineTo(12.521f, 4.9f)
                close()
            }
        }.build()

        return _Restart!!
    }

@Suppress("ObjectPropertyName")
private var _Restart: ImageVector? = null
