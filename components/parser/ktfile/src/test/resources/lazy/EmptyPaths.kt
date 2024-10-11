package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.EmptyPaths: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "EmptyPaths",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 18.0f,
        viewportHeight = 18.0f,
    ).apply {
        path { }
        path { }
        path { }
    }.build()
}
