package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.compose.codeviewer.core.CodeEditor
import io.github.composegears.valkyrie.compose.codeviewer.core.rememberCodeHighlight

@Composable
fun XmlCodeViewer(
    text: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit = {},
) {
    val highlights = rememberCodeHighlight(codeBlock = text)

    CodeEditor(
        modifier = modifier,
        highlights = highlights,
        onValueChange = onChange,
    )
}
