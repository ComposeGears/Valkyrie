package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val WithoutPath: ImageVector
 get() {
  if (_WithoutPath != null) {
   return _WithoutPath!!
  }
  _WithoutPath = ImageVector.Builder(
   name = "WithoutPath",
   defaultWidth = 24.dp,
   defaultHeight = 24.dp,
   viewportWidth = 18f,
   viewportHeight = 18f
  ).build()

  return _WithoutPath!!
 }

@Suppress("ObjectPropertyName")
private var _WithoutPath: ImageVector? = null
