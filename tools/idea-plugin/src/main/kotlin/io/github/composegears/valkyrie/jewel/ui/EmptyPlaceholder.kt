package io.github.composegears.valkyrie.jewel.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun EmptyPlaceholder(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(16.dp),
        text = message,
        style = JewelTheme.typography.h4TextStyle,
    )
}

@Preview
@Composable
private fun EmptyPlaceholderPreview() = PreviewTheme(alignment = Alignment.Center) {
    EmptyPlaceholder(message = "Message")
}
