package io.github.composegears.valkyrie.ui.foundation.highlights

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.PhraseLocation
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun CodeTextView(
  highlights: Highlights,
  modifier: Modifier = Modifier,
) {
  Text(
    modifier = modifier,
    style = MaterialTheme.typography.bodySmall,
    text = buildAnnotatedString {
      append(highlights.getCode())

      highlights.getHighlights()
        .filterIsInstance<ColorHighlight>()
        .forEach { highlight ->
          val location = highlight.location

          if (location.end - location.start != 1) {
            addStyle(
              style = SpanStyle(color = Color(highlight.rgb).copy(alpha = 1f)),
              start = location.start,
              end = location.end,
            )
          }
        }

      highlights.getHighlights()
        .filterIsInstance<BoldHighlight>()
        .forEach {
          addStyle(
            SpanStyle(
              fontWeight = FontWeight.Bold,
              textDecoration = TextDecoration.Underline,
            ),
            start = it.location.start,
            end = it.location.end,
          )
        }
    },
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

  CodeTextView(highlights)
}
