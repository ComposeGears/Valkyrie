package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.Error: ImageVector
    get() {
        if (_Error != null) {
            return _Error!!
        }
        _Error = ImageVector.Builder(
            name = "Error",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(fill = SolidColor(Color(0xFFE55765))) {
                moveTo(8f, 8f)
                moveToRelative(-7f, 0f)
                arcToRelative(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, 14f, 0f)
                arcToRelative(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, -14f, 0f)
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(9f, 5f)
                curveTo(9f, 4.448f, 8.552f, 4f, 8f, 4f)
                curveTo(7.448f, 4f, 7f, 4.448f, 7f, 5f)
                verticalLineTo(7.5f)
                curveTo(7f, 8.052f, 7.448f, 8.5f, 8f, 8.5f)
                curveTo(8.552f, 8.5f, 9f, 8.052f, 9f, 7.5f)
                lineTo(9f, 5f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(8f, 12f)
                curveTo(8.552f, 12f, 9f, 11.552f, 9f, 11f)
                curveTo(9f, 10.448f, 8.552f, 10f, 8f, 10f)
                curveTo(7.448f, 10f, 7f, 10.448f, 7f, 11f)
                curveTo(7f, 11.552f, 7.448f, 12f, 8f, 12f)
                close()
            }
        }.build()

        return _Error!!
    }

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
private var _Error: ImageVector? = null
