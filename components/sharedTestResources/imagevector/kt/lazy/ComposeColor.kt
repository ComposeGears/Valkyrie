package io.github.composegears.valkyrie.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ValkyrieIcons.ComposeColor: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "ComposeColor",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 18f,
        viewportHeight = 18f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(1f, 1f)
            lineTo(5f, 1f)
            lineTo(5f, 5f)
            lineTo(1f, 5f)
            close()
        }
        path(fill = SolidColor(Color.DarkGray)) {
            moveTo(6f, 1f)
            lineTo(10f, 1f)
            lineTo(10f, 5f)
            lineTo(6f, 5f)
            close()
        }
        path(fill = SolidColor(Color.Gray)) {
            moveTo(11f, 1f)
            lineTo(15f, 1f)
            lineTo(15f, 5f)
            lineTo(11f, 5f)
            close()
        }
        path(fill = SolidColor(Color.LightGray)) {
            moveTo(1f, 6f)
            lineTo(5f, 6f)
            lineTo(5f, 10f)
            lineTo(1f, 10f)
            close()
        }
        path(fill = SolidColor(Color.White)) {
            moveTo(6f, 6f)
            lineTo(10f, 6f)
            lineTo(10f, 10f)
            lineTo(6f, 10f)
            close()
        }
        path(fill = SolidColor(Color.Red)) {
            moveTo(11f, 6f)
            lineTo(15f, 6f)
            lineTo(15f, 10f)
            lineTo(11f, 10f)
            close()
        }
        path(fill = SolidColor(Color.Green)) {
            moveTo(1f, 11f)
            lineTo(5f, 11f)
            lineTo(5f, 15f)
            lineTo(1f, 15f)
            close()
        }
        path(fill = SolidColor(Color.Blue)) {
            moveTo(6f, 11f)
            lineTo(10f, 11f)
            lineTo(10f, 15f)
            lineTo(6f, 15f)
            close()
        }
        path(fill = SolidColor(Color.Yellow)) {
            moveTo(11f, 11f)
            lineTo(15f, 11f)
            lineTo(15f, 15f)
            lineTo(11f, 15f)
            close()
        }
        path(fill = SolidColor(Color.Cyan)) {
            moveTo(4.5f, 4.5f)
            lineTo(8.5f, 4.5f)
            lineTo(8.5f, 8.5f)
            lineTo(4.5f, 8.5f)
            close()
        }
        path(fill = SolidColor(Color.Magenta)) {
            moveTo(9.5f, 9.5f)
            lineTo(12.5f, 9.5f)
            lineTo(12.5f, 12.5f)
            lineTo(9.5f, 12.5f)
            close()
        }
        path(fill = SolidColor(Color.Red.copy(alpha = 0.5019608f))) {
            moveTo(1f, 8.5f)
            lineTo(4.5f, 8.5f)
            lineTo(4.5f, 12f)
            lineTo(1f, 12f)
            close()
        }
        path(fill = SolidColor(Color.Green.copy(alpha = 0.5019608f))) {
            moveTo(5f, 8.5f)
            lineTo(8.5f, 8.5f)
            lineTo(8.5f, 12f)
            lineTo(5f, 12f)
            close()
        }
        path(fill = SolidColor(Color.Blue.copy(alpha = 0.5019608f))) {
            moveTo(9f, 8.5f)
            lineTo(12.5f, 8.5f)
            lineTo(12.5f, 12f)
            lineTo(9f, 12f)
            close()
        }
        path(fill = SolidColor(Color.Black.copy(alpha = 0.2509804f))) {
            moveTo(2f, 2f)
            lineTo(6f, 2f)
            lineTo(6f, 6f)
            lineTo(2f, 6f)
            close()
        }
        path(fill = SolidColor(Color.White.copy(alpha = 0.7490196f))) {
            moveTo(6f, 2f)
            lineTo(10f, 2f)
            lineTo(10f, 6f)
            lineTo(6f, 6f)
            close()
        }
    }.build()
}
