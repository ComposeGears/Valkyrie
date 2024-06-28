package io.github.composegears.valkyrie.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

fun codeBlockAnnotatedString(
    codeBlock: String,
    highlightText: String
): AnnotatedString = buildAnnotatedString {
    withStyle(SpanStyle(fontWeight = FontWeight.ExtraLight)) {
        append(codeBlock)
    }

    val indexes = allIndexesOf(substring = highlightText, text = codeBlock)
    indexes.forEach {
        addStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            ),
            start = it,
            end = it + highlightText.length
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
