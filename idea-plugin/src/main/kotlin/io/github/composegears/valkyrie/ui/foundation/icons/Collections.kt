package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val ValkyrieIcons.Collections: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    materialIcon(name = "Collections") {
        materialPath {
            moveTo(22.0f, 16.0f)
            lineTo(22.0f, 4.0f)
            curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
            lineTo(8.0f, 2.0f)
            curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
            verticalLineToRelative(12.0f)
            curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
            horizontalLineToRelative(12.0f)
            curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
            close()
            moveTo(11.0f, 12.0f)
            lineToRelative(2.03f, 2.71f)
            lineTo(16.0f, 11.0f)
            lineToRelative(4.0f, 5.0f)
            lineTo(8.0f, 16.0f)
            lineToRelative(3.0f, -4.0f)
            close()
            moveTo(2.0f, 6.0f)
            verticalLineToRelative(14.0f)
            curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
            horizontalLineToRelative(14.0f)
            verticalLineToRelative(-2.0f)
            lineTo(4.0f, 20.0f)
            lineTo(4.0f, 6.0f)
            lineTo(2.0f, 6.0f)
            close()
        }
    }
}
