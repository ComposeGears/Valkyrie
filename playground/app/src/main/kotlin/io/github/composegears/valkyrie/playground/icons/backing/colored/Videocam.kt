package io.github.composegears.valkyrie.playground.icons.backing.colored

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.playground.icons.backing.BackingIcons

val BackingIcons.Colored.Videocam: ImageVector
    get() {
        if (_Videocam != null) {
            return _Videocam!!
        }
        _Videocam = ImageVector.Builder(
            name = "Colored.Videocam",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFF2CA5D1))) {
                moveTo(15f, 8f)
                verticalLineToRelative(8f)
                horizontalLineTo(5f)
                verticalLineTo(8f)
                horizontalLineToRelative(10f)
                moveToRelative(1f, -2f)
                horizontalLineTo(4f)
                curveToRelative(-0.55f, 0f, -1f, 0.45f, -1f, 1f)
                verticalLineToRelative(10f)
                curveToRelative(0f, 0.55f, 0.45f, 1f, 1f, 1f)
                horizontalLineToRelative(12f)
                curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
                verticalLineToRelative(-3.5f)
                lineToRelative(4f, 4f)
                verticalLineToRelative(-11f)
                lineToRelative(-4f, 4f)
                verticalLineTo(7f)
                curveToRelative(0f, -0.55f, -0.45f, -1f, -1f, -1f)
                close()
            }
        }.build()

        return _Videocam!!
    }

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
private var _Videocam: ImageVector? = null
