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

val ValkyrieIcons.Folder: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    materialIcon(name = "Folder") {
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
}

@Preview
@Composable
private fun FolderPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = ValkyrieIcons.Folder, contentDescription = null)
    }
}
