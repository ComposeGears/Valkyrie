package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val ValkyrieIcons.Filled.FlatPackage: ImageVector
    get() {
        _FlatPackage?.let { return it }

        val FlatPackage = ImageVector.Builder(
            name = "Filled.FlatPackage",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 18f,
            viewportHeight = 18f
        ).build()

        _FlatPackage = FlatPackage
        return FlatPackage
    }

@Suppress("ObjectPropertyName")
private var _FlatPackage: ImageVector? = null
