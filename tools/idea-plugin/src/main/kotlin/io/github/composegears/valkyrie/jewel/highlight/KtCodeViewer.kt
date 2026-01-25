package io.github.composegears.valkyrie.jewel.highlight

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.PhraseLocation
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.jewel.tooling.debugBounds
import io.github.composegears.valkyrie.sdk.compose.highlights.core.buildAnnotatedString
import io.github.composegears.valkyrie.sdk.compose.highlights.core.rememberCodeHighlight
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun KtCodeViewer(
    highlights: Highlights,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        style = JewelTheme.typography.editorTextStyle,
        text = highlights.buildAnnotatedString(),
    )
}

@Preview
@Composable
private fun KtCodeViewerPreview() = PreviewTheme(alignment = Alignment.Center) {
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
        isDark = JewelTheme.isDark,
    )

    KtCodeViewer(
        modifier = Modifier.debugBounds(),
        highlights = highlights,
    )
}
