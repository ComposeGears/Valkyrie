package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

data class HighlightColors(
    val keywordColor: Color,
    val identifierColor: Color,
    val defaultColor: Color,
) {
    companion object {
        val DARK = HighlightColors(
            keywordColor = Color(0xFFC87631),
            identifierColor = Color(0xFF8B6C9B),
            defaultColor = Color(0xFFBCBEC4),
        )
        val LIGHT = HighlightColors(
            keywordColor = Color(0xFF4A6EC9),
            identifierColor = Color(0xFF881395),
            defaultColor = Color(0xFF1F1F1F),
        )
    }
}

fun getHighlightedCode(
    code: String,
    colors: HighlightColors,
) = buildAnnotatedString {
    val keywords = listOf("val", "by", "null", "if", "return", "private", "var", "get")
    val identifiers = listOf("NONE")

    var i = 0
    while (i < code.length) {
        val matchedKeyword = keywords.find { code.startsWith(it, i) }
        val matchedIdentifier = identifiers.find { code.startsWith(it, i) }
        val isBackingProperty = code.startsWith("_", i)

        when {
            matchedKeyword != null -> {
                withStyle(style = SpanStyle(color = colors.keywordColor)) {
                    append(matchedKeyword)
                }
                i += matchedKeyword.length
            }
            matchedIdentifier != null -> {
                withStyle(style = SpanStyle(color = colors.identifierColor)) {
                    append(matchedIdentifier)
                }
                i += matchedIdentifier.length
            }
            code[i].isWhitespace() -> {
                append(code[i])
                i++
            }
            isBackingProperty -> {
                while (code[i] != ' ') {
                    withStyle(style = SpanStyle(color = colors.identifierColor)) {
                        append(code[i])
                        i++
                    }
                }
            }
            else -> {
                withStyle(style = SpanStyle(color = colors.defaultColor)) {
                    append(code[i])
                }
                i++
            }
        }
    }
}
