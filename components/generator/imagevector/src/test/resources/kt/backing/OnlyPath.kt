package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val ValkyrieIcons.OnlyPath: ImageVector
    get() {
        if (_OnlyPath != null) {
            return _OnlyPath!!
        }
        _OnlyPath = ImageVector.Builder(
            name = "OnlyPath",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).apply {
            path {
                moveTo(6.75f, 12.127f)
                lineTo(3.623f, 9f)
                lineTo(2.558f, 10.057f)
                lineTo(6.75f, 14.25f)
                lineTo(15.75f, 5.25f)
                lineTo(14.693f, 4.192f)
                lineTo(6.75f, 12.127f)
                close()
            }
        }.build()

        return _OnlyPath!!
    }

@Suppress("ObjectPropertyName")
private var _OnlyPath: ImageVector? = null
