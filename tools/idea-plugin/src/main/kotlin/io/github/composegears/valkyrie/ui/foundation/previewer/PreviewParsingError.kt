package io.github.composegears.valkyrie.ui.foundation.previewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation.Url
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.colors.primaryColor
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.core.extensions.cast
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

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
            Icon(
                key = AllIconsKeys.General.Error,
                contentDescription = null,
            )
            ErrorMessage()
        }
        WeightSpacer(weight = 0.7f)
    }
}

@Composable
private fun ErrorMessage() {
    val uriHandler = LocalUriHandler.current

    val underlineColor = JewelTheme.primaryColor
    val annotatedString = buildAnnotatedString {
        append("Failed to preview ImageVector, please ")

        withLink(
            link = Url(
                url = "https://github.com/ComposeGears/Valkyrie/issues",
                linkInteractionListener = { uriHandler.openUri(it.cast<Url>().url) },
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

    Text(text = annotatedString)
}

@Preview
@Composable
private fun PreviewParsingErrorPreview() = PreviewTheme {
    PreviewParsingError()
}
