package io.github.composegears.valkyrie.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val ValkyrieIcons.WithoutPath: ImageVector
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

@Preview
@Composable
private fun WithoutPathPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = ValkyrieIcons.WithoutPath, contentDescription = null)
    }
}
