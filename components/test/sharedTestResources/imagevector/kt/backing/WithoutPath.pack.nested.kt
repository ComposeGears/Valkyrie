package io.github.composegears.valkyrie.icons.colored

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.icons.ValkyrieIcons

val ValkyrieIcons.Colored.WithoutPath: ImageVector
    get() {
        if (_WithoutPath != null) {
            return _WithoutPath!!
        }
        _WithoutPath = ImageVector.Builder(
            name = "Colored.WithoutPath",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).build()

        return _WithoutPath!!
    }

@Suppress("ObjectPropertyName")
private var _WithoutPath: ImageVector? = null
