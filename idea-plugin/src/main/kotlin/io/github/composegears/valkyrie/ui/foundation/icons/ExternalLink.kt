package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val ValkyrieIcons.ExternalLink: ImageVector
    get() {
        if (_ExternalLink != null) {
            return _ExternalLink!!
        }
        _ExternalLink = ImageVector.Builder(
            name = "ExternalLink",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f,
        ).apply {
            path(
                fill = SolidColor(Color(0xFF6C707E)),
                pathFillType = PathFillType.EvenOdd,
            ) {
                moveTo(5f, 5.5f)
                curveTo(5f, 5.224f, 5.224f, 5f, 5.5f, 5f)
                horizontalLineTo(11f)
                verticalLineTo(10.5f)
                curveTo(11f, 10.776f, 10.776f, 11f, 10.5f, 11f)
                curveTo(10.224f, 11f, 10f, 10.776f, 10f, 10.5f)
                verticalLineTo(6.707f)
                lineTo(4.854f, 11.854f)
                curveTo(4.658f, 12.049f, 4.342f, 12.049f, 4.146f, 11.854f)
                curveTo(3.951f, 11.658f, 3.951f, 11.342f, 4.146f, 11.146f)
                lineTo(9.293f, 6f)
                horizontalLineTo(5.5f)
                curveTo(5.224f, 6f, 5f, 5.776f, 5f, 5.5f)
                close()
            }
        }.build()

        return _ExternalLink!!
    }

@Suppress("ObjectPropertyName")
private var _ExternalLink: ImageVector? = null
