package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.unit.dp

val ValkyrieIcons.ClipPath: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "ClipPath",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        group(
            clipPathData = addPathNodes("M 12.0 2.0 C 6.48 2.0 2.0 6.48 2.0 12.0 s 4.48 10.0 10.0 10.0 s 10.0 -4.48 10.0 -10.0 S 17.52 2.0 12.0 2.0 Z")
        ) {
            addPath(
                fill = SolidColor(Color(0xFFFF0000)),
                pathData = addPathNodes("M 0.0 0.0 h 24.0 v 24.0 h -24.0 Z")
            )
        }
    }.build()
}
