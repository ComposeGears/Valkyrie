package io.github.composegears.valkyrie.ui.foundation.icons

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val ValkyrieIcons.Backspace: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    materialIcon(name = "AutoMirrored.Filled.Backspace", autoMirror = true) {
        materialPath {
            moveTo(22.0f, 3.0f)
            lineTo(7.0f, 3.0f)
            curveToRelative(-0.69f, 0.0f, -1.23f, 0.35f, -1.59f, 0.88f)
            lineTo(0.0f, 12.0f)
            lineToRelative(5.41f, 8.11f)
            curveToRelative(0.36f, 0.53f, 0.9f, 0.89f, 1.59f, 0.89f)
            horizontalLineToRelative(15.0f)
            curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
            lineTo(24.0f, 5.0f)
            curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
            close()
            moveTo(19.0f, 15.59f)
            lineTo(17.59f, 17.0f)
            lineTo(14.0f, 13.41f)
            lineTo(10.41f, 17.0f)
            lineTo(9.0f, 15.59f)
            lineTo(12.59f, 12.0f)
            lineTo(9.0f, 8.41f)
            lineTo(10.41f, 7.0f)
            lineTo(14.0f, 10.59f)
            lineTo(17.59f, 7.0f)
            lineTo(19.0f, 8.41f)
            lineTo(15.41f, 12.0f)
            lineTo(19.0f, 15.59f)
            close()
        }
    }
}

@Preview
@Composable
private fun BackspacePreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = ValkyrieIcons.Backspace, contentDescription = null)
    }
}
