package io.github.composegears.valkyrie.parser.ktfile.expected

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
