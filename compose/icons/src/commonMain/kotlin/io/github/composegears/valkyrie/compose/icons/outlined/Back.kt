package io.github.composegears.valkyrie.compose.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Outlined.Back: ImageVector
    get() {
        if (_Back != null) {
            return _Back!!
        }
        _Back = ImageVector.Builder(
            name = "Outlined.Back",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(640f, 852.31f)
                lineTo(267.69f, 480f)
                lineTo(640f, 107.69f)
                lineToRelative(42.54f, 42.54f)
                lineTo(352.77f, 480f)
                lineToRelative(329.77f, 329.77f)
                lineTo(640f, 852.31f)
                close()
            }
        }.build()

        return _Back!!
    }

@Suppress("ObjectPropertyName")
private var _Back: ImageVector? = null
