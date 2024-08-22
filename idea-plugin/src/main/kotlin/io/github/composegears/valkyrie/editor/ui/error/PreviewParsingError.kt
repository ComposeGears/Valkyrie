package io.github.composegears.valkyrie.editor.ui.error

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.ClickableText
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.icons.Error
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.platform.rememberBrowser
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun PreviewParsingError(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WeightSpacer(weight = 0.3f)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        ) {
            Image(
                imageVector = ValkyrieIcons.Error,
                contentDescription = null,
            )
            ErrorMessage()
        }
        WeightSpacer(weight = 0.7f)
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ErrorMessage() {
    val browser = rememberBrowser()

    val underlineColor = MaterialTheme.colorScheme.primary
    val annotatedString = buildAnnotatedString {
        append("Failed to preview ImageVector, please ")

        withAnnotation(UrlAnnotation("https://github.com/ComposeGears/Valkyrie/issues")) {
            append(
                AnnotatedString(
                    "submit issue",
                    spanStyle = SpanStyle(
                        color = underlineColor,
                        textDecoration = TextDecoration.Underline,
                    ),
                ),
            )
        }
        append(" with reproducer.")
    }
    ClickableText(
        annotatedString = annotatedString,
        onClick = browser::open,
    )
}

@Preview
@Composable
private fun PreviewParsingErrorPreview() = PreviewTheme {
    PreviewParsingError()
}
