package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.EmptyPaths: ImageVector
    get() {
        if (_EmptyPaths != null) {
            return _EmptyPaths!!
        }
        _EmptyPaths = ImageVector.Builder(
            name = "EmptyPaths",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f,
        ).apply {
            path { }
            path { }
            path { }
        }.build()

        return _EmptyPaths!!
    }

@Suppress("ObjectPropertyName")
private var _EmptyPaths: ImageVector? = null
