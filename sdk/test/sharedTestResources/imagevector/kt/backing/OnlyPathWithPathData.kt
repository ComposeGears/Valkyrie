package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPath
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

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
            addPath(
                pathData = addPathNodes("M 6.75 12.127 L 3.623 9.0 L 2.558 10.057 L 6.75 14.25 L 15.75 5.25 L 14.693 4.192 L 6.75 12.127 Z")
            )
        }.build()

        return _OnlyPath!!
    }

@Suppress("ObjectPropertyName")
private var _OnlyPath: ImageVector? = null
