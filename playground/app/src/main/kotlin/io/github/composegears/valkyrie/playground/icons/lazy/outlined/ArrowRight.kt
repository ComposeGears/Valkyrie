package io.github.composegears.valkyrie.playground.icons.lazy.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.playground.icons.lazy.LazyIcons
import kotlin.LazyThreadSafetyMode

val LazyIcons.Outlined.ArrowRight: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
  ImageVector.Builder(
    name = "Outlined.ArrowRight",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
  ).apply {
    path(fill = SolidColor(Color(0xFF232F34))) {
      moveTo(3f, 12.688f)
      curveTo(3f, 13.24f, 3.448f, 13.688f, 4f, 13.688f)
      lineTo(17.452f, 13.688f)
      lineTo(11.513f, 19.531f)
      curveTo(11.125f, 19.913f, 11.114f, 20.535f, 11.488f, 20.931f)
      lineTo(11.793f, 21.254f)
      curveTo(12.181f, 21.663f, 12.829f, 21.671f, 13.227f, 21.273f)
      lineTo(22f, 12.5f)
      lineTo(13.207f, 3.707f)
      curveTo(12.817f, 3.317f, 12.183f, 3.317f, 11.793f, 3.707f)
      lineTo(11.532f, 3.968f)
      curveTo(11.142f, 4.358f, 11.142f, 4.991f, 11.531f, 5.381f)
      lineTo(17.452f, 11.313f)
      lineTo(4f, 11.313f)
      curveTo(3.448f, 11.313f, 3f, 11.76f, 3f, 12.313f)
      verticalLineTo(12.688f)
      close()
    }
  }.build()
}
