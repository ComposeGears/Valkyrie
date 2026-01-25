package io.github.composegears.valkyrie.ui.screen.settings.tabs.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.editNavStack
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigationFadeInOut
import com.composegears.tiamat.navigation.NavController
import com.composegears.tiamat.navigation.NavDestination.Companion.toNavEntry
import com.intellij.openapi.ui.MessageDialogBuilder
import io.github.composegears.valkyrie.jewel.platform.rememberCurrentProject
import io.github.composegears.valkyrie.jewel.settings.Group
import io.github.composegears.valkyrie.jewel.settings.GroupSpacing
import io.github.composegears.valkyrie.jewel.settings.InfoSettingsRow
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.shared.Mode.Editor
import io.github.composegears.valkyrie.shared.Mode.IconPack
import io.github.composegears.valkyrie.shared.Mode.ImageVectorToXml
import io.github.composegears.valkyrie.shared.Mode.Simple
import io.github.composegears.valkyrie.shared.Mode.Unspecified
import io.github.composegears.valkyrie.shared.Mode.WebImport
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.screen.settings.GeneralSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.LocalComponent
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Link
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.icons.AllIconsKeys

val GeneralSettingsScreen by navDestination {
    val navController = navController()

    val viewModel = viewModel<SettingsViewModel>(viewModelStoreOwner = navController)
    val generalSettings by viewModel.generalSettings.collectAsState()

    GeneralSettingsUi(
        generalSettings = generalSettings,
        onClearSettings = { yesNo ->
            if (yesNo) {
                viewModel.clearSettings()
                openIntro(navController)
            }
        },
        onChangeMode = {
            viewModel.resetMode()
            openIntro(navController)
        },
    )
}

@OptIn(ExperimentalJewelApi::class)
@Composable
private fun GeneralSettingsUi(
    generalSettings: GeneralSettings,
    onClearSettings: (Boolean) -> Unit,
    onChangeMode: () -> Unit,
) {
    val component = LocalComponent.current

    val mode = generalSettings.mode
    val initialMode = remember { mode }
    val currentMode = remember(mode) {
        when (mode) {
            Unspecified -> initialMode
            else -> mode
        }
    }

    VerticallyScrollableContainer {
        Column {
            Group(
                text = stringResource("settings.general.group.info"),
                itemSpacing = 16.dp,
            ) {
                val name = when (currentMode) {
                    Simple -> stringResource("settings.general.mode.simple")
                    IconPack -> stringResource("settings.general.mode.iconpack")
                    Editor -> stringResource("settings.general.mode.editor")
                    ImageVectorToXml -> stringResource("settings.general.mode.imagevector.xml")
                    Unspecified, WebImport -> stringResource("settings.general.mode.unspecified")
                }
                InfoSettingsRow(
                    text = stringResource("settings.general.current.mode", name),
                    infoText = stringResource("settings.general.current.mode.description"),
                    trailing = {
                        Link(
                            text = stringResource("settings.general.current.mode.action"),
                            onClick = { if (mode != Unspecified) onChangeMode() },
                        )
                    },
                )

                val currentProject = rememberCurrentProject()
                InfoSettingsRow(
                    text = stringResource("settings.general.destination"),
                    infoText = when {
                        generalSettings.iconPackDestination.isEmpty() -> stringResource("settings.general.destination.unspecified")
                        else -> "~${generalSettings.iconPackDestination.replace(currentProject.path.orEmpty(), "")}"
                    },
                )
                InfoSettingsRow(
                    text = stringResource("settings.general.package"),
                    infoText = generalSettings.packageName.ifEmpty { stringResource("settings.general.package.unspecified") },
                )
            }
            GroupSpacing()
            Group(
                text = stringResource("settings.general.group.danger.zone"),
                startComponent = {
                    Icon(
                        key = AllIconsKeys.General.Warning,
                        contentDescription = stringResource("accessibility.warning"),
                    )
                },
            ) {
                Link(
                    modifier = Modifier.padding(start = 8.dp),
                    text = stringResource("settings.general.reset"),
                    onClick = {
                        val yesNo = MessageDialogBuilder.yesNo(
                            title = message("settings.general.reset.dialog.title"),
                            message = message("settings.general.reset.dialog.message"),
                        ).ask(component)

                        onClearSettings(yesNo)
                    },
                )
            }
            GroupSpacing()
        }
    }
}

private fun openIntro(navController: NavController) {
    navController.parent?.run {
        editNavStack(transition = navigationFadeInOut()) { _ -> listOf(IntroScreen.toNavEntry()) }
    }
}

@Preview
@Composable
private fun GeneralSettingsPreview() = PreviewTheme {
    var isClearSettings by rememberMutableState<Boolean?> { null }

    GeneralSettingsUi(
        generalSettings = GeneralSettings(
            mode = Simple,
            packageName = "io.github.composegears.valkyrie",
            iconPackDestination = "path/to/import",
        ),
        onChangeMode = {},
        onClearSettings = { isClearSettings = it },
    )
    Text(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp),
        text = "Clear settings: $isClearSettings",
    )
}
