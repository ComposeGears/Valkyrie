package io.github.composegears.valkyrie.playground.icons.backing.outlined

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.playground.icons.backing.BackingIcons

val BackingIcons.Outlined.Brightness: ImageVector
  get() {
    if (_Brightness != null) {
      return _Brightness!!
    }
    _Brightness = ImageVector.Builder(
      name = "Outlined.Brightness",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f,
    ).apply {
      path(fill = SolidColor(Color(0xFF232F34))) {
        moveTo(20f, 8.69f)
        lineTo(20f, 4f)
        lineTo(15.31f, 4f)
        lineTo(12f, 0.69f)
        lineTo(8.69f, 4f)
        lineTo(4f, 4f)
        lineTo(4f, 8.69f)
        lineTo(0.69f, 12f)
        lineTo(4f, 15.31f)
        lineTo(4f, 20f)
        lineTo(8.69f, 20f)
        lineTo(12f, 23.31f)
        lineTo(15.31f, 20f)
        lineTo(20f, 20f)
        lineTo(20f, 15.31f)
        lineTo(23.31f, 12f)
        lineTo(20f, 8.69f)
        close()
        moveTo(12f, 18f)
        curveTo(8.69f, 18f, 6f, 15.31f, 6f, 12f)
        curveTo(6f, 8.69f, 8.69f, 6f, 12f, 6f)
        curveTo(15.31f, 6f, 18f, 8.69f, 18f, 12f)
        curveTo(18f, 15.31f, 15.31f, 18f, 12f, 18f)
        close()
        moveTo(12f, 8f)
        curveTo(9.79f, 8f, 8f, 9.79f, 8f, 12f)
        curveTo(8f, 14.21f, 9.79f, 16f, 12f, 16f)
        curveTo(14.21f, 16f, 16f, 14.21f, 16f, 12f)
        curveTo(16f, 9.79f, 14.21f, 8f, 12f, 8f)
        close()
      }
    }.build()

    return _Brightness!!
  }

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
private var _Brightness: ImageVector? = null
