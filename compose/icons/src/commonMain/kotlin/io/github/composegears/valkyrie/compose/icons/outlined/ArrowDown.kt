package io.github.composegears.valkyrie.compose.icons.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons

val ValkyrieIcons.Outlined.ArrowDown: ImageVector
    get() {
        if (_ArrowDown != null) {
            return _ArrowDown!!
        }
        _ArrowDown = ImageVector.Builder(
            name = "Outlined.ArrowDown",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(480f, 580f)
                lineTo(276f, 376f)
                lineToRelative(20f, -20f)
                lineToRelative(184f, 184f)
                lineToRelative(184f, -184f)
                lineToRelative(20f, 20f)
                lineToRelative(-204f, 204f)
                close()
            }
        }.build()

        return _ArrowDown!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowDown: ImageVector? = null
