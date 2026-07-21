package io.github.composegears.valkyrie.icons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val LinearGradientAlpha: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "LinearGradientAlpha",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color(0x001D1C1C),
                    0.68f to Color(0xFF1D1C1C)
                ),
                start = Offset(0f, 0f),
                end = Offset(24f, 24f)
            )
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(24f)
            verticalLineToRelative(24f)
            horizontalLineTo(0f)
            close()
        }
    }.build()
}
