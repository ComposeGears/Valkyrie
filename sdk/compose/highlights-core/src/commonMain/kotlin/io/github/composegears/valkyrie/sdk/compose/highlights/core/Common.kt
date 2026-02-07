package io.github.composegears.valkyrie.sdk.compose.highlights.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.PhraseLocation
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes

@Composable
fun rememberCodeHighlight(
    codeBlock: String,
    isDark: Boolean,
    vararg emphasisLocation: PhraseLocation,
): Highlights {
    return remember(isDark, codeBlock, emphasisLocation) {
        Highlights.Builder()
            .code(codeBlock)
            .language(SyntaxLanguage.KOTLIN)
            .theme(SyntaxThemes.darcula(darkMode = isDark))
            .emphasis(*emphasisLocation)
            .build()
    }
}

fun Highlights.buildAnnotatedString() = buildAnnotatedString {
    append(getCode())

    getHighlights()
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

    getHighlights()
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
}

fun getEmphasisLocations(
    codeBlock: String,
    highlightText: String,
): Array<PhraseLocation> {
    val indexes = allIndexesOf(substring = highlightText, text = codeBlock)

    return Array(indexes.size) {
        PhraseLocation(
            start = indexes[it],
            end = indexes[it] + highlightText.length,
        )
    }
}

private fun allIndexesOf(substring: String, text: String): List<Int> {
    return buildList {
        var index = text.indexOf(substring)
        while (index >= 0) {
            add(index)
            index = text.indexOf(substring, index + 1)
        }
    }
}
