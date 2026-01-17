package io.github.composegears.valkyrie.jewel

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.Divider

@Composable
fun HorizontalDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier.fillMaxWidth(),
        orientation = Orientation.Horizontal,
    )
}

@Preview
@Composable
private fun HorizontalDividerPreview() = PreviewTheme(alignment = Alignment.Center) {
    HorizontalDivider()
}
