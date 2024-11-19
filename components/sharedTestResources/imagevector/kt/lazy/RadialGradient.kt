package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val RadialGradient: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "RadialGradient",
        defaultWidth = 100.dp,
        defaultHeight = 20.dp,
        viewportWidth = 100f,
        viewportHeight = 20f
    ).apply {
        path(
            fill = Brush.radialGradient(
                colorStops = arrayOf(
                    0.19f to Color(0xFFD53A42),
                    0.39f to Color(0xFFDF7A40),
                    0.59f to Color(0xFFF0A941),
                    1f to Color(0xFFFFFFF0)
                ),
                center = Offset(0f, 10f),
                radius = 100f
            )
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(100f)
            verticalLineToRelative(20f)
            horizontalLineToRelative(-100f)
            close()
        }
    }.build()
}
