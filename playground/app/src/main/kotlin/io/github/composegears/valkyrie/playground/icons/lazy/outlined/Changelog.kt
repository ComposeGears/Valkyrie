package io.github.composegears.valkyrie.playground.icons.lazy.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.playground.icons.lazy.LazyIcons
import kotlin.LazyThreadSafetyMode

val LazyIcons.Outlined.Changelog: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
  ImageVector.Builder(
    name = "Outlined.Changelog",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
  ).apply {
    path(fill = SolidColor(Color(0xFF232F34))) {
      moveTo(20.317f, 8.555f)
      curveTo(19.777f, 7.255f, 20.569f, 5.719f, 19.425f, 4.575f)
      curveTo(18.281f, 3.431f, 16.745f, 4.223f, 15.445f, 3.682f)
      curveTo(14.152f, 3.148f, 13.591f, 1.5f, 12f, 1.5f)
      curveTo(10.409f, 1.5f, 9.849f, 3.15f, 8.555f, 3.682f)
      curveTo(7.256f, 4.226f, 5.72f, 3.43f, 4.575f, 4.575f)
      curveTo(3.433f, 5.717f, 4.223f, 7.256f, 3.682f, 8.555f)
      curveTo(3.148f, 9.848f, 1.5f, 10.409f, 1.5f, 12f)
      curveTo(1.5f, 13.591f, 3.15f, 14.151f, 3.682f, 15.445f)
      curveTo(4.223f, 16.745f, 3.431f, 18.281f, 4.575f, 19.425f)
      curveTo(5.719f, 20.569f, 7.255f, 19.777f, 8.555f, 20.317f)
      curveTo(9.848f, 20.851f, 10.409f, 22.5f, 12f, 22.5f)
      curveTo(13.591f, 22.5f, 14.151f, 20.85f, 15.445f, 20.317f)
      curveTo(16.745f, 19.777f, 18.281f, 20.569f, 19.425f, 19.425f)
      curveTo(20.569f, 18.281f, 19.777f, 16.745f, 20.317f, 15.445f)
      curveTo(20.851f, 14.152f, 22.5f, 13.591f, 22.5f, 12f)
      curveTo(22.5f, 10.409f, 20.85f, 9.849f, 20.317f, 8.555f)
      close()
      moveTo(11.25f, 7.5f)
      curveTo(11.25f, 7.091f, 11.592f, 6.75f, 12f, 6.75f)
      curveTo(12.408f, 6.75f, 12.75f, 7.091f, 12.75f, 7.5f)
      verticalLineTo(12.75f)
      curveTo(12.75f, 13.158f, 12.408f, 13.5f, 12f, 13.5f)
      curveTo(11.592f, 13.5f, 11.25f, 13.158f, 11.25f, 12.75f)
      verticalLineTo(7.5f)
      close()
      moveTo(12f, 17.25f)
      curveTo(11.555f, 17.25f, 11.131f, 16.967f, 10.961f, 16.556f)
      curveTo(10.696f, 15.918f, 11.095f, 15.158f, 11.781f, 15.022f)
      curveTo(12.466f, 14.885f, 13.125f, 15.435f, 13.125f, 16.125f)
      curveTo(13.125f, 16.738f, 12.613f, 17.25f, 12f, 17.25f)
      close()
    }
  }.build()
}
