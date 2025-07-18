package io.github.composegears.valkyrie.ui.screen.settings.tabs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation.Url
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.colored.ValkyrieLogo
import io.github.composegears.valkyrie.compose.util.dim
import io.github.composegears.valkyrie.extensions.cast
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider
import io.github.composegears.valkyrie.ui.foundation.icons.ExternalLink
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.rememberBrowser
import io.github.composegears.valkyrie.ui.screen.intro.util.rememberPluginVersion
import io.github.composegears.valkyrie.util.stringResource

val AboutSettingsScreen by navDestination<Unit> {
    AboutSettingsUi()
}

@Composable
private fun AboutSettingsUi() {
    Column {
        VerticalSpacer(16.dp)
        CenterVerticalRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                imageVector = ValkyrieIcons.Colored.ValkyrieLogo,
                contentDescription = null,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Valkyrie Plugin",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = rememberPluginVersion(),
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.dim(),
                )
            }
        }
        VerticalSpacer(16.dp)
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        VerticalSpacer(16.dp)

        val browser = rememberBrowser()

        ClickableUrl(
            modifier = Modifier.padding(horizontal = 16.dp),
            link = "https://github.com/ComposeGears/Valkyrie",
            text = stringResource("settings.about.sourcecode"),
            onUrlClick = browser::open,
        )
        VerticalSpacer(16.dp)
        ClickableUrl(
            modifier = Modifier.padding(horizontal = 16.dp),
            link = "https://github.com/ComposeGears/Valkyrie/issues",
            text = stringResource("settings.about.issue"),
            onUrlClick = browser::open,
        )
    }
}

@Composable
private fun ClickableUrl(
    link: String,
    text: String,
    modifier: Modifier = Modifier,
    onUrlClick: (String) -> Unit,
) {
    val linkColor = MaterialTheme.colorScheme.primary

    CenterVerticalRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val annotatedString = buildAnnotatedString {
            withLink(
                link = Url(
                    url = link,
                    linkInteractionListener = { onUrlClick(it.cast<Url>().url) },
                ),
                block = {
                    append(
                        AnnotatedString(
                            text = text,
                            spanStyle = SpanStyle(color = linkColor),
                        ),
                    )
                },
            )
            append(" ")
            appendInlineContent(id = "icon")
        }

        val inlineTextContent = InlineTextContent(
            placeholder = Placeholder(
                width = LocalTextStyle.current.fontSize,
                height = LocalTextStyle.current.fontSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
        ) {
            Icon(
                imageVector = ValkyrieIcons.ExternalLink,
                contentDescription = null,
                tint = linkColor,
            )
        }

        BasicText(
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            inlineContent = mapOf("icon" to inlineTextContent),
        )
    }
}

@Preview
@Composable
private fun AboutSettingsPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    AboutSettingsUi()
}
