package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val ValkyrieIcons.Folder: ImageVector
  get() {
    if (_folder != null) {
      return _folder!!
    }
    _folder = materialIcon(name = "Folder") {
      materialPath {
        moveTo(9.17f, 6.0f)
        lineToRelative(2.0f, 2.0f)
        horizontalLineTo(20.0f)
        verticalLineToRelative(10.0f)
        horizontalLineTo(4.0f)
        verticalLineTo(6.0f)
        horizontalLineToRelative(5.17f)
        moveTo(10.0f, 4.0f)
        horizontalLineTo(4.0f)
        curveToRelative(-1.1f, 0.0f, -1.99f, 0.9f, -1.99f, 2.0f)
        lineTo(2.0f, 18.0f)
        curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
        horizontalLineToRelative(16.0f)
        curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
        verticalLineTo(8.0f)
        curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
        horizontalLineToRelative(-8.0f)
        lineToRelative(-2.0f, -2.0f)
        close()
      }
    }
    return _folder!!
  }

@Suppress("ObjectPropertyName", "ktlint:standard:backing-property-naming")
private var _folder: ImageVector? = null
