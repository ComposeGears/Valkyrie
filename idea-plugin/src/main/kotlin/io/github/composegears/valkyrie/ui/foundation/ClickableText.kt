package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalTextApi::class)
@Composable
fun ClickableText(
    annotatedString: AnnotatedString,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isOnHoverLink by rememberMutableState { false }

    ClickableText(
        modifier = modifier.pointerHoverIcon(
            icon = if (isOnHoverLink) PointerIcon.Hand else PointerIcon.Default,
        ),
        text = annotatedString,
        onHover = { index ->
            if (index != null) {
                isOnHoverLink = annotatedString.getUrlAnnotations(index, index + 1).firstOrNull() != null
            }
        },
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        onClick = { offset ->
            annotatedString.getUrlAnnotations(offset, offset + 1)
                .firstOrNull()
                ?.let { urlAnnotation ->
                    onClick(urlAnnotation.item.url)
                }
        },
    )
}

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
private fun ClickableTextPreview() = PreviewTheme {
    val annotatedString = buildAnnotatedString {
        append("Sample text with ")
        withAnnotation(UrlAnnotation("https://github.com/")) {
            append(
                AnnotatedString(
                    text = "url",
                    spanStyle = SpanStyle(textDecoration = TextDecoration.Underline),
                ),
            )
        }
    }
    ClickableText(
        annotatedString = annotatedString,
        onClick = {},
    )
}
