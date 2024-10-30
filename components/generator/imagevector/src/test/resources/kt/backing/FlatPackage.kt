package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val FlatPackage: ImageVector
    get() {
        if (_FlatPackage != null) {
            return _FlatPackage!!
        }
        _FlatPackage = ImageVector.Builder(
            name = "FlatPackage",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).build()

        return _FlatPackage!!
    }

@Suppress("ObjectPropertyName")
private var _FlatPackage: ImageVector? = null
