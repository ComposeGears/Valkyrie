package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.TransparentFillColor: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "TransparentFillColor",
        defaultWidth = 192.dp,
        defaultHeight = 192.dp,
        viewportWidth = 192f,
        viewportHeight = 192f
    ).apply {
        path(
            stroke = SolidColor(Color(0xFF000000)),
            strokeLineWidth = 12f,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(22f, 57.26f)
            verticalLineToRelative(84.74f)
            curveToRelative(0f, 5.52f, 4.48f, 10f, 10f, 10f)
            horizontalLineToRelative(18f)
            curveToRelative(3.31f, 0f, 6f, -2.69f, 6f, -6f)
            verticalLineTo(95.06f)
            lineToRelative(40f, 30.28f)
            lineToRelative(40f, -30.28f)
            verticalLineToRelative(50.94f)
            curveToRelative(0f, 3.31f, 2.69f, 6f, 6f, 6f)
            horizontalLineToRelative(18f)
            curveToRelative(5.52f, 0f, 10f, -4.48f, 10f, -10f)
            verticalLineTo(57.26f)
            curveToRelative(0f, -13.23f, -15.15f, -20.75f, -25.68f, -12.74f)
            lineTo(96f, 81.26f)
            lineTo(47.68f, 44.53f)
            curveToRelative(-10.52f, -8.01f, -25.68f, 3.48f, -25.68f, 12.73f)
            close()
        }
    }.build()
}
