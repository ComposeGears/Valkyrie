package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ExpectedIconWithImportMember = ImageVector.Builder(
    name = "Tbank",
    defaultWidth = 40.0.dp,
    defaultHeight = 40.0.dp,
    viewportWidth = 40.0f,
    viewportHeight = 40.0f,
).apply {
    path(fill = SolidColor(Color(0xFFFFFFFF))) {
        moveTo(7.57f, 9.24f)
        horizontalLineTo(32.427f)
        verticalLineTo(21.37f)
        curveTo(32.427f, 24.708f, 30.718f, 27.793f, 27.942f, 29.462f)
        lineTo(19.999f, 34.24f)
        lineTo(12.055f, 29.462f)
        curveTo(9.28f, 27.793f, 7.57f, 24.708f, 7.57f, 21.37f)
        lineTo(7.57f, 9.24f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF333333)),
        pathFillType = EvenOdd,
    ) {
        moveTo(14.8f, 16.5f)
        verticalLineTo(19.878f)
        curveTo(15.274f, 19.356f, 16.336f, 19.003f, 17.322f, 19.003f)
        horizontalLineTo(18.393f)
        verticalLineTo(22.93f)
        curveTo(18.393f, 23.974f, 18.102f, 25.397f, 17.669f, 25.9f)
        horizontalLineTo(22.329f)
        curveTo(21.897f, 25.396f, 21.607f, 23.975f, 21.607f, 22.932f)
        verticalLineTo(19.003f)
        horizontalLineTo(22.678f)
        curveTo(23.664f, 19.003f, 24.726f, 19.356f, 25.2f, 19.878f)
        verticalLineTo(16.5f)
        horizontalLineTo(14.8f)
        close()
    }
}.build()
