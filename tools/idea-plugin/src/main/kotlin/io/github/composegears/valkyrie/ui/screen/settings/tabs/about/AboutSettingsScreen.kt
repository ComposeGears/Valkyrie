package io.github.composegears.valkyrie.ui.screen.settings.tabs.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.TiamatPreview
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.ValkyrieBuildConfig
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.settings.GroupSpacing
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.rememberShimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.sdk.compose.icons.colored.PluginIcon
import io.github.composegears.valkyrie.ui.screen.settings.tabs.about.domain.ContributorUiModel
import io.github.composegears.valkyrie.ui.screen.settings.tabs.about.ui.ContributorItem
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.ExternalLink
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.typography

val AboutSettingsScreen by navDestination {
    val viewModel = viewModel { AboutSettingsViewModel() }
    val contributors by viewModel.contributors.collectAsState()

    AboutSettingsUi(contributors = contributors)
}

@Composable
private fun AboutSettingsUi(contributors: List<ContributorUiModel>) {
    val shimmer = rememberShimmer()

    VerticallyScrollableContainer {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Image(
                    modifier = Modifier.size(48.dp),
                    imageVector = ValkyrieIcons.Colored.PluginIcon,
                    contentDescription = stringResource("accessibility.logo"),
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource("settings.about.plugin.name"),
                        style = JewelTheme.typography.h3TextStyle,
                    )
                    InfoText(
                        text = ValkyrieBuildConfig.VERSION,
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
            Spacer(16.dp)
            ExternalLink(
                uri = "https://plugins.jetbrains.com/plugin/24786-valkyrie",
                text = stringResource("settings.rate.marketplace"),
            )
            Spacer(16.dp)
            HorizontalDivider()
            Spacer(16.dp)
            Text(
                text = stringResource("settings.about.contributors"),
                style = JewelTheme.typography.h4TextStyle,
                maxLines = 1,
            )
            Spacer(12.dp)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                contributors.forEach { contributor ->
                    ContributorItem(contributor = contributor, shimmer = shimmer)
                }
            }
            GroupSpacing()
        }
    }
}

@Preview
@Composable
private fun AboutSettingsPreview() = PreviewTheme {
    TiamatPreview(AboutSettingsScreen)
}
