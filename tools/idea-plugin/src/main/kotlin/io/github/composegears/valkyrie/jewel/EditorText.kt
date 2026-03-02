package io.github.composegears.valkyrie.jewel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.jewel.highlight.HighlightedCode
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun EditorText(
    code: HighlightedCode,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = code.value,
        style = JewelTheme.typography.editorTextStyle,
    )
}

@Preview
@Composable
private fun EditorTextPreview() = PreviewTheme(alignment = Alignment.Center) {
    Text(text = "editor text style")
}
