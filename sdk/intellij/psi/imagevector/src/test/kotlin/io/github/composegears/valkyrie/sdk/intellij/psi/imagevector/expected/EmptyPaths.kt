package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ExpectedEmptyPaths = ImageVector.Builder(
    name = "EmptyPaths",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 18f,
    viewportHeight = 18f,
).apply {
    path { }
    path { }
    path { }
}.build()
