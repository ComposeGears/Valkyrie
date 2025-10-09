package io.github.composegears.valkyrie.psi.imagevector.expected

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.PathFillType

val ExpectedMaterialIcon = materialIcon(name = "Filled.Settings", autoMirror = true) {
    materialPath(
        fillAlpha = 0.5f,
        strokeAlpha = 0.6f,
        pathFillType = PathFillType.EvenOdd,
    ) {
        moveTo(19.14f, 12.94f)
        close()
    }
}

val ExpectedMaterialIconWithoutParam = materialIcon(name = "Bell") {
    materialPath {
        moveTo(18.0f, 16.0f)
        lineTo(18.0f, 11.0f)
        curveTo(18.0f, 7.9297f, 16.3594f, 5.3594f, 13.5f, 4.6797f)
        lineTo(13.5f, 4.0f)
        curveTo(13.5f, 3.1719f, 12.8281f, 2.5f, 12.0f, 2.5f)
        curveTo(11.1719f, 2.5f, 10.5f, 3.1719f, 10.5f, 4.0f)
        lineTo(10.5f, 4.6797f)
        curveTo(7.6289f, 5.3594f, 6.0f, 7.9219f, 6.0f, 11.0f)
        lineTo(6.0f, 16.0f)
        lineTo(4.0f, 18.0f)
        lineTo(4.0f, 19.0f)
        lineTo(20.0f, 19.0f)
        lineTo(20.0f, 18.0f)
        close()
        moveTo(12.0f, 22.0f)
        curveTo(13.1016f, 22.0f, 14.0f, 21.1016f, 14.0f, 20.0f)
        lineTo(10.0f, 20.0f)
        curveTo(10.0f, 21.1016f, 10.8906f, 22.0f, 12.0f, 22.0f)
        close()
        moveTo(12.0f, 22.0f)
    }
}
