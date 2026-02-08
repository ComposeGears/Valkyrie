package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.expected

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

val ExpectedAddPathWithPathData = ImageVector.Builder(
    name = "SinglePath",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 18f,
    viewportHeight = 18f,
).apply {
    addPath(
        fill = SolidColor(Color(0xFF000000)),
        pathData = addPathNodes("M 6.75 12.127 L 3.623 9 L 2.558 10.057 L 6.75 14.25 L 15.75 5.25 L 14.693 4.192 L 6.75 12.127 Z"),
    )
}.build()
