package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected

import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.path

val ExpectedMaterialIconWithPath = materialIcon(name = "ArrowRight") {
    path(
        fill = SolidColor(Color.Black),
        stroke = null,
        strokeLineWidth = 1f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1f,
    ) {
        moveTo(10f, 17f)
        lineTo(15f, 12f)
        lineTo(10f, 7f)
        verticalLineTo(17f)
        close()
    }
}
