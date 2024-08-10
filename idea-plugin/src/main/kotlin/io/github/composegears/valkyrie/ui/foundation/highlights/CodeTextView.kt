package io.github.composegears.valkyrie.ui.foundation.highlights

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
                .forEach {
                    addStyle(
                        style = SpanStyle(color = Color(it.rgb).copy(alpha = 1f)),
                        start = it.location.start,
                        end = it.location.end,
                    )
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
