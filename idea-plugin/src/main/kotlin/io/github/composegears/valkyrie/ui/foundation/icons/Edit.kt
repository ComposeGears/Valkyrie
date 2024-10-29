package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.Edit: ImageVector
    get() {
        if (_Edit != null) {
            return _Edit!!
        }
        _Edit = ImageVector.Builder(
            name = "Edit",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF6C707E)),
                strokeLineWidth = 1f,
            ) {
                moveTo(11.597f, 7.655f)
                lineTo(13.688f, 5.56f)
                curveTo(14.105f, 5.154f, 14.1f, 4.496f, 13.695f, 4.086f)
                lineTo(12.027f, 2.314f)
                lineTo(12.022f, 2.309f)
                curveTo(11.612f, 1.9f, 10.942f, 1.893f, 10.533f, 2.311f)
                lineTo(8.387f, 4.444f)
                moveTo(11.597f, 7.655f)
                lineTo(8.387f, 4.444f)
                moveTo(11.597f, 7.655f)
                lineTo(5.74f, 13.5f)
                horizontalLineTo(2.5f)
                lineTo(2.5f, 10.32f)
                lineTo(8.387f, 4.444f)
            }
        }.build()

        return _Edit!!
    }

@Suppress("ObjectPropertyName")
private var _Edit: ImageVector? = null
