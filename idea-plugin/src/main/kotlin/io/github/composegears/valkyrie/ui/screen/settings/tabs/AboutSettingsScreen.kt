package io.github.composegears.valkyrie.ui.screen.settings.tabs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.ui.foundation.ClickableText
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.dim
import io.github.composegears.valkyrie.ui.foundation.disabled
import io.github.composegears.valkyrie.ui.foundation.icons.ExternalLink
import io.github.composegears.valkyrie.ui.foundation.icons.PluginIcon
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.platform.rememberBrowser
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.intro.util.rememberPluginVersion

val AboutSettingsScreen by navDestination<Unit> {
    AboutSettingsUi()
}

@Composable
private fun AboutSettingsUi() {
    Column {
        VerticalSpacer(16.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                imageVector = ValkyrieIcons.PluginIcon,
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
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = LocalContentColor.current.disabled(),
            thickness = Dp.Hairline,
        )
        VerticalSpacer(16.dp)

        val browser = rememberBrowser()

        ClickableUrl(
            modifier = Modifier.padding(horizontal = 16.dp),
            link = "https://github.com/ComposeGears/Valkyrie",
            text = "Source code",
            onUrlClick = browser::open,
        )
        VerticalSpacer(16.dp)
        ClickableUrl(
            modifier = Modifier.padding(horizontal = 16.dp),
            link = "https://github.com/ComposeGears/Valkyrie/issues",
            text = "Submit issue or idea",
            onUrlClick = browser::open,
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ClickableUrl(
    link: String,
    text: String,
    modifier: Modifier = Modifier,
    onUrlClick: (String) -> Unit,
) {
    val linkColor = MaterialTheme.colorScheme.primary

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val annotatedString = buildAnnotatedString {
            withAnnotation(UrlAnnotation(link)) {
                append(
                    AnnotatedString(
                        text = text,
                        spanStyle = SpanStyle(color = linkColor),
                    ),
                )
            }
        }
        ClickableText(
            annotatedString = annotatedString,
            onClick = onUrlClick,
        )
        Icon(
            imageVector = ValkyrieIcons.ExternalLink,
            contentDescription = null,
            tint = linkColor,
        )
    }
}

@Preview
@Composable
private fun AboutSettingsPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    AboutSettingsUi()
}
