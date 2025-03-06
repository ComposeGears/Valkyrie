package io.github.composegears.valkyrie.icons.filled

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.icons.ValkyrieIcons

val ValkyrieIcons.Filled.WithoutPath: ImageVector by lazy(LazyThreadSafetyMode.NONE) {
    ImageVector.Builder(
        name = "Filled.WithoutPath",
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
        Image(imageVector = ValkyrieIcons.Filled.WithoutPath, contentDescription = null)
    }
}
