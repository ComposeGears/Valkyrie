package io.github.composegears.valkyrie.ui.foundation.components.previewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation.Url
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.extensions.cast
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.icons.Error
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.rememberBrowser

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

@Composable
private fun ErrorMessage() {
    val browser = rememberBrowser()

    val underlineColor = MaterialTheme.colorScheme.primary
    val annotatedString = buildAnnotatedString {
        append("Failed to preview ImageVector, please ")

        withLink(
            link = Url(
                url = "https://github.com/ComposeGears/Valkyrie/issues",
                linkInteractionListener = { browser.open(it.cast<Url>().url) },
            ),
            block = {
                append(
                    AnnotatedString(
                        text = "submit issue",
                        spanStyle = SpanStyle(
                            color = underlineColor,
                            textDecoration = TextDecoration.Underline,
                        ),
                    ),
                )
            },
        )
        append(" with reproducer.")
    }

    BasicText(
        text = annotatedString,
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Preview
@Composable
private fun PreviewParsingErrorPreview() = PreviewTheme {
    PreviewParsingError()
}
