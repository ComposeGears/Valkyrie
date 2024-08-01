package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val ValkyrieIcons.EmptyImageVector: ImageVector
    get() {
        if (_EmptyImageVector != null) {
            return _EmptyImageVector!!
        }
        _EmptyImageVector = ImageVector.Builder(
            name = "EmptyImageVector",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f,
        ).build()

        return _EmptyImageVector!!
    }

@Suppress("ObjectPropertyName")
private var _EmptyImageVector: ImageVector? = null
