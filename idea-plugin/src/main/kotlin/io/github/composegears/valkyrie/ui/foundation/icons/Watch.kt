package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val ValkyrieIcons.Watch: ImageVector
    get() {
        if (_Watch != null) {
            return _Watch!!
        }
        _Watch = ImageVector.Builder(
            name = "Watch",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                fill = SolidColor(Color(0xFF6C707E)),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(6.625f, 6.547f)
                curveTo(6.114f, 5.625f, 5.13f, 5f, 4f, 5f)
                curveTo(2.694f, 5f, 1.583f, 5.835f, 1.171f, 7f)
                horizontalLineTo(0.5f)
                curveTo(0.224f, 7f, 0f, 7.224f, 0f, 7.5f)
                curveTo(0f, 7.776f, 0.224f, 8f, 0.5f, 8f)
                horizontalLineTo(1f)
                curveTo(1f, 9.657f, 2.343f, 11f, 4f, 11f)
                curveTo(5.657f, 11f, 7f, 9.657f, 7f, 8f)
                curveTo(7f, 7.448f, 7.448f, 7f, 8f, 7f)
                curveTo(8.552f, 7f, 9f, 7.448f, 9f, 8f)
                curveTo(9f, 9.657f, 10.343f, 11f, 12f, 11f)
                curveTo(13.657f, 11f, 15f, 9.657f, 15f, 8f)
                horizontalLineTo(15.5f)
                curveTo(15.776f, 8f, 16f, 7.776f, 16f, 7.5f)
                curveTo(16f, 7.224f, 15.776f, 7f, 15.5f, 7f)
                horizontalLineTo(14.829f)
                curveTo(14.417f, 5.835f, 13.306f, 5f, 12f, 5f)
                curveTo(10.87f, 5f, 9.886f, 5.625f, 9.375f, 6.547f)
                curveTo(9.016f, 6.208f, 8.532f, 6f, 8f, 6f)
                curveTo(7.468f, 6f, 6.984f, 6.208f, 6.625f, 6.547f)
                close()
                moveTo(4f, 10f)
                curveTo(5.105f, 10f, 6f, 9.105f, 6f, 8f)
                curveTo(6f, 6.895f, 5.105f, 6f, 4f, 6f)
                curveTo(2.895f, 6f, 2f, 6.895f, 2f, 8f)
                curveTo(2f, 9.105f, 2.895f, 10f, 4f, 10f)
                close()
                moveTo(10f, 8f)
                curveTo(10f, 9.105f, 10.895f, 10f, 12f, 10f)
                curveTo(13.105f, 10f, 14f, 9.105f, 14f, 8f)
                curveTo(14f, 6.895f, 13.105f, 6f, 12f, 6f)
                curveTo(10.895f, 6f, 10f, 6.895f, 10f, 8f)
                close()
            }
        }.build()

        return _Watch!!
    }

@Suppress("ObjectPropertyName")
private var _Watch: ImageVector? = null
