package io.github.composegears.valkyrie.jewel

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider

@Composable
fun VerticalDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier.fillMaxHeight(),
        orientation = Orientation.Vertical,
    )
}

@Preview
@Composable
private fun VerticalDividerPreview() = PreviewTheme {
    VerticalDivider()
}
