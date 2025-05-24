package io.github.composegears.valkyrie.ui.foundation.highlights.core

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.PhraseLocation
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun CodeViewer(
    highlights: Highlights,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
        text = highlights.buildAnnotatedString(),
    )
}

@Preview
@Composable
private fun CodeTextViewPreview() = PreviewTheme {
    val highlights = rememberCodeHighlight(
        codeBlock = """
            package io.github.composegears.valkyrie

            val Pack.MyIcon: ImageVector
                get() {
                    if (_MyIcon != null) {
                    return _MyIcon!!
                }
                ...
            }
        """.trimIndent(),
        emphasisLocation = arrayOf(PhraseLocation(start = 8, end = 39)),
    )

    CodeViewer(highlights)
}
