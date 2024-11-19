package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.SeveralPath: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "SeveralPath",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 18f,
        viewportHeight = 18f
    ).apply {
        path(fill = SolidColor(Color(0xFFE676FF))) {
            moveTo(6.75f, 12.127f)
            lineTo(3.623f, 9f)
            lineTo(2.558f, 10.057f)
            lineTo(6.75f, 14.25f)
            lineTo(15.75f, 5.25f)
            lineTo(14.693f, 4.192f)
            lineTo(6.75f, 12.127f)
            close()
        }
        path(fill = SolidColor(Color(0xFFFF00FF))) {
            moveTo(6.75f, 12.127f)
            lineTo(3.623f, 9f)
            lineTo(2.558f, 10.057f)
            lineTo(6.75f, 14.25f)
            lineTo(15.75f, 5.25f)
            lineTo(14.693f, 4.192f)
            lineTo(6.75f, 12.127f)
            close()
        }
    }.build()
}
