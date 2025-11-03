package io.github.composegears.valkyrie.ui.screen.intro

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.navigation.NavDestination
import io.github.composegears.valkyrie.FeatureFlag.ICON_EDITOR_FEATURE_ENABLED
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.outlined.Conversion
import io.github.composegears.valkyrie.compose.icons.outlined.Editor
import io.github.composegears.valkyrie.compose.icons.outlined.FileImport
import io.github.composegears.valkyrie.compose.ui.InfoCard
import io.github.composegears.valkyrie.shared.Mode
import io.github.composegears.valkyrie.shared.Mode.Editor
import io.github.composegears.valkyrie.shared.Mode.IconPack
import io.github.composegears.valkyrie.shared.Mode.Simple
import io.github.composegears.valkyrie.shared.Mode.Unspecified
import io.github.composegears.valkyrie.shared.Mode.WebImport
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.icons.BatchProcessing
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.editor.EditorSelectScreen
import io.github.composegears.valkyrie.ui.screen.intro.util.rememberPluginVersion
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.IconPackCreationScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.ui.screen.webimport.WebImportFlow
import io.github.composegears.valkyrie.util.stringResource

val IntroScreen: NavDestination<Unit> by navDestination {

    val navController = navController()

    IntroScreenUI(
        openSettings = {
            navController.navigate(dest = SettingsScreen)
        },
        onModeChange = {
            when (it) {
                Simple -> navController.navigate(dest = SimpleConversionScreen)
                IconPack -> navController.navigate(dest = IconPackCreationScreen)
                Editor -> navController.navigate(dest = EditorSelectScreen)
                WebImport -> navController.navigate(dest = WebImportFlow)
                Unspecified -> {}
            }
        },
    )
}

@Composable
private fun IntroScreenUI(
    openSettings: () -> Unit,
    onModeChange: (Mode) -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WeightSpacer(weight = 0.3f)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource("intro.header"),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                VerticalSpacer(42.dp)
                Text(
                    text = stringResource("intro.subheader"),
                    style = MaterialTheme.typography.labelSmall,
                    color = LocalContentColor.current.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                )
                VerticalSpacer(8.dp)
                InfoCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = { onModeChange(Simple) },
                    icon = ValkyrieIcons.Outlined.Conversion,
                    title = stringResource("intro.card.simple.title"),
                    description = stringResource("intro.card.simple.description"),
                )
                VerticalSpacer(16.dp)
                InfoCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = { onModeChange(IconPack) },
                    icon = ValkyrieIcons.BatchProcessing,
                    title = stringResource("intro.card.iconpack.title"),
                    description = stringResource("intro.card.iconpack.description"),
                )
                if (ICON_EDITOR_FEATURE_ENABLED) {
                    VerticalSpacer(16.dp)
                    InfoCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onClick = { onModeChange(Editor) },
                        image = ValkyrieIcons.Outlined.Editor,
                        title = stringResource("intro.card.editor.title"),
                        description = stringResource("intro.card.editor.description"),
                    )
                }
                VerticalSpacer(24.dp)
                HorizontalDivider(modifier = Modifier.fillMaxWidth(0.1f))
                VerticalSpacer(24.dp)
                InfoCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = { onModeChange(WebImport) },
                    icon = ValkyrieIcons.Outlined.FileImport,
                    title = stringResource("intro.card.webimport.title"),
                    description = stringResource("intro.card.webimport.description"),
                )
            }
            WeightSpacer(weight = 0.7f)
        }
        SettingsAction(
            modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.TopEnd),
            openSettings = openSettings,
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            text = rememberPluginVersion(),
        )
    }
}

@Preview
@Composable
private fun IntroScreenUIPreview() = PreviewTheme {
    IntroScreenUI(
        openSettings = {},
        onModeChange = {},
    )
}
