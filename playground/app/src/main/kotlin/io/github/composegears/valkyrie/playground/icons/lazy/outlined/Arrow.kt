package io.github.composegears.valkyrie.playground.icons.lazy.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.playground.icons.lazy.LazyIcons
import kotlin.LazyThreadSafetyMode

val LazyIcons.Outlined.Arrow: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
  ImageVector.Builder(
    name = "Outlined.Arrow",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
  ).apply {
    path(fill = SolidColor(Color(0xFF232F34))) {
      moveTo(12.413f, 3.565f)
      curveTo(11.97f, 2.675f, 10.647f, 2.704f, 10.243f, 3.613f)
      lineTo(3.411f, 18.958f)
      curveTo(2.959f, 19.974f, 4.01f, 21.005f, 5.016f, 20.533f)
      curveTo(6.869f, 19.664f, 10.429f, 17.88f, 11.614f, 17.618f)
      curveTo(12.942f, 17.767f, 16.565f, 19.644f, 18.494f, 20.503f)
      curveTo(19.522f, 20.961f, 20.559f, 19.878f, 20.056f, 18.871f)
      lineTo(12.413f, 3.565f)
      close()
    }
  }.build()
}
