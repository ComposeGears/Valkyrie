package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val WithoutPath: ImageVector
 get() {
  _WithoutPath?.let { return it }

  val WithoutPath = ImageVector.Builder(
   name = "WithoutPath",
   defaultWidth = 24.dp,
   defaultHeight = 24.dp,
   viewportWidth = 18f,
   viewportHeight = 18f
  ).build()

  _WithoutPath = WithoutPath
  return WithoutPath
 }

@Suppress("ObjectPropertyName")
private var _WithoutPath: ImageVector? = null
