package io.github.composegears.valkyrie.jewel.highlight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import io.github.composegears.valkyrie.jewel.EditorText
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.jewel.tooling.debugBounds
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.bridge.code.highlighting.CodeHighlighterFactory
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.code.MimeType
import org.jetbrains.jewel.foundation.theme.JewelTheme

@OptIn(ExperimentalJewelApi::class)
@Composable
fun rememberCodeHighlight(
    text: String,
    mimeType: MimeType = MimeType.Known.KOTLIN,
    style: UnderlineDsl.() -> Unit = {},
): HighlightedCode {
    val dsl = UnderlineDsl(text).apply(style)

    val project = LocalProject.current
    val underlineColor = JewelTheme.globalColors.outlines.warning
    val codeHighlighter = remember { CodeHighlighterFactory.getInstance(project).createHighlighter() }

    val highlightedCode by codeHighlighter
        .highlight(code = text, mimeType = mimeType)
        .map {
            if (dsl.ranges.isNotEmpty()) {
                buildAnnotatedString {
                    append(it)
                    dsl.ranges.forEach { range ->
                        addStyle(
                            style = SpanStyle(
                                background = underlineColor,
                                textDecoration = TextDecoration.Underline,
                            ),
                            start = range.first,
                            end = range.last + 1,
                        )
                    }
                }
            } else {
                it
            }
        }
        .collectAsState(AnnotatedString(text))

    return HighlightedCode(highlightedCode)
}

@JvmInline
value class HighlightedCode(val value: AnnotatedString)

@DslMarker
annotation class UnderlineDslMarker

@UnderlineDslMarker
class UnderlineDsl internal constructor(
    private val text: String,
) {

    internal val ranges = mutableListOf<IntRange>()

    fun underline(range: IntRange) {
        require(range.first >= 0) { "Range start < 0" }
        require(range.last < text.length) { "Range out of bounds" }
        require(range.first <= range.last) { "Invalid range" }

        ranges += range
    }

    fun underline(highlightText: String) {
        require(highlightText.isNotEmpty())

        var index = 0
        while (true) {
            index = text.indexOf(highlightText, startIndex = index)
            if (index == -1) break

            ranges += index..<index + highlightText.length
            index += highlightText.length
        }
    }
}

@Preview
@Composable
private fun KtCodeViewerPreview() = ProjectPreviewTheme(alignment = Alignment.Center) {
    val highlightedCode = rememberCodeHighlight(
        text = """
         package io.github.composegears.valkyrie
 
         val Pack.MyIcon: ImageVector
             get() {
                 if (_MyIcon != null) {
                 return _MyIcon!!
             }
             ...
         }
        """.trimIndent(),
        style = {
            underline(8..38)
            underline(highlightText = "_MyIcon")
        },
    )

    EditorText(
        code = highlightedCode,
        modifier = Modifier.debugBounds(),
    )
}
