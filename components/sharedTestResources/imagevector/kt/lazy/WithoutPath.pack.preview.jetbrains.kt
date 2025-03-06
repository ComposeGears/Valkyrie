package io.github.composegears.valkyrie.icons

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val ValkyrieIcons.WithoutPath: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "WithoutPath",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 18f,
        viewportHeight = 18f
    ).build()
}

@Preview
@Composable
private fun WithoutPathPreview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = ValkyrieIcons.WithoutPath, contentDescription = null)
    }
}
