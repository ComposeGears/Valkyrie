package io.github.composegears.valkyrie.ui.screen.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.FeatureFlag.ICON_EDITOR_FEATURE_ENABLED
import io.github.composegears.valkyrie.FeatureFlag.KT_TO_SVG_ENABLED
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.InfoCard
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.BatchProcessing
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.Conversion
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.Editor
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.FileImport
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.KtSvg
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.KtXml
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.SvgXml
import io.github.composegears.valkyrie.shared.Mode
import io.github.composegears.valkyrie.shared.Mode.Editor
import io.github.composegears.valkyrie.shared.Mode.IconPack
import io.github.composegears.valkyrie.shared.Mode.ImageVectorToXml
import io.github.composegears.valkyrie.shared.Mode.Simple
import io.github.composegears.valkyrie.shared.Mode.SvgToXml
import io.github.composegears.valkyrie.shared.Mode.Unspecified
import io.github.composegears.valkyrie.shared.Mode.WebImport
import io.github.composegears.valkyrie.ui.screen.editor.EditorSelectScreen
import io.github.composegears.valkyrie.ui.screen.intro.util.rememberPluginVersion
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.IconPackModeScreen
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.picker.ImageVectorPickerScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.picker.SimplePickerScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.picker.SvgXmlPickerScreen
import io.github.composegears.valkyrie.ui.screen.webimport.WebImportFlow
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.typography

val IntroScreen by navDestination {

    val navController = navController()

    IntroScreenUI(
        openSettings = {
            navController.navigate(dest = SettingsScreen)
        },
        onModeChange = {
            when (it) {
                Simple -> navController.navigate(dest = SimplePickerScreen)
                IconPack -> navController.navigate(dest = IconPackModeScreen)
                Editor -> navController.navigate(dest = EditorSelectScreen)
                WebImport -> navController.navigate(dest = WebImportFlow)
                ImageVectorToXml -> navController.navigate(dest = ImageVectorPickerScreen)
                SvgToXml -> navController.navigate(dest = SvgXmlPickerScreen)
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
    Column {
        Toolbar {
            WeightSpacer()
            SettingsAction(openSettings = openSettings)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            VerticallyScrollableContainer {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stringResource("intro.modes.header"),
                        color = JewelTheme.globalColors.text.info,
                    )
                    InfoCard(
                        onClick = { onModeChange(Simple) },
                        icon = ValkyrieIcons.Outlined.Conversion,
                        title = stringResource("intro.card.simple.title"),
                        description = stringResource("intro.card.simple.description"),
                    )
                    InfoCard(
                        onClick = { onModeChange(IconPack) },
                        icon = ValkyrieIcons.Outlined.BatchProcessing,
                        title = stringResource("intro.card.iconpack.title"),
                        description = stringResource("intro.card.iconpack.description"),
                    )
                    Spacer(8.dp)
                    HorizontalDivider(modifier = Modifier.width(72.dp))
                    Spacer(8.dp)
                    Text(
                        text = stringResource("intro.tools.header"),
                        color = JewelTheme.globalColors.text.info,
                    )
                    if (ICON_EDITOR_FEATURE_ENABLED) {
                        InfoCard(
                            onClick = { onModeChange(Editor) },
                            icon = ValkyrieIcons.Outlined.Editor,
                            title = stringResource("intro.card.editor.title"),
                            description = stringResource("intro.card.editor.description"),
                        )
                    }
                    InfoCard(
                        onClick = { onModeChange(SvgToXml) },
                        icon = ValkyrieIcons.Outlined.SvgXml,
                        title = stringResource("intro.card.svg.to.xml.title"),
                        description = stringResource("intro.card.svg.to.xml.description"),
                    )
                    InfoCard(
                        onClick = { onModeChange(ImageVectorToXml) },
                        icon = ValkyrieIcons.Outlined.KtXml,
                        title = stringResource("intro.card.imagevectortoxml.title"),
                        description = stringResource("intro.card.imagevectortoxml.description"),
                    )
                    if (KT_TO_SVG_ENABLED) {
                        InfoCard(
                            onClick = { },
                            icon = ValkyrieIcons.Outlined.KtSvg,
                            title = stringResource("intro.card.imagevectortosvg.title"),
                            description = stringResource("intro.card.imagevectortosvg.description"),
                        )
                    }
                    InfoCard(
                        onClick = { onModeChange(WebImport) },
                        icon = ValkyrieIcons.Outlined.FileImport,
                        title = stringResource("intro.card.webimport.title"),
                        description = stringResource("intro.card.webimport.description"),
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 8.dp),
                style = JewelTheme.typography.small,
                text = rememberPluginVersion(),
            )
        }
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
