package io.github.composegears.valkyrie.ui.screen.settings.tabs.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.colored.ValkyrieLogo
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.settings.GroupSpacing
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.intro.util.rememberPluginVersion
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.ExternalLink
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.typography

val AboutSettingsScreen by navDestination {
    AboutSettingsUi()
}

@Composable
private fun AboutSettingsUi() {
    VerticallyScrollableContainer {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(16.dp)
            CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Image(
                    modifier = Modifier.size(48.dp),
                    imageVector = ValkyrieIcons.Colored.ValkyrieLogo,
                    contentDescription = stringResource("accessibility.logo"),
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource("settings.about.plugin.name"),
                        style = JewelTheme.typography.h3TextStyle,
                    )
                    InfoText(
                        text = rememberPluginVersion(),
                        style = JewelTheme.typography.small,
                    )
                }
            }
            Spacer(16.dp)
            HorizontalDivider()
            Spacer(16.dp)
            ExternalLink(
                uri = "https://github.com/ComposeGears/Valkyrie",
                text = stringResource("settings.about.sourcecode"),
            )
            Spacer(16.dp)
            ExternalLink(
                uri = "https://github.com/ComposeGears/Valkyrie/issues",
                text = stringResource("settings.about.issue"),
            )
            GroupSpacing()
        }
    }
}

@Preview
@Composable
private fun AboutSettingsPreview() = PreviewTheme {
    AboutSettingsUi()
}
