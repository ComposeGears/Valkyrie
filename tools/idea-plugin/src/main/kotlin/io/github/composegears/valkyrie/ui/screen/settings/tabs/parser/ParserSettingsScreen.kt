package io.github.composegears.valkyrie.ui.screen.settings.tabs.parser

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.TiamatPreview
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.jewel.settings.CheckboxSettingsRow
import io.github.composegears.valkyrie.jewel.settings.Group
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.settings.ParserSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.ParserStrictMode
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer

val ParserSettingsScreen by navDestination {
    val viewModel = viewModel<SettingsViewModel>(viewModelStoreOwner = navController())
    val parserSettings by viewModel.parserSettings.collectAsState()

    ParserSettingsUi(
        parserSettings = parserSettings,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ParserSettingsUi(
    modifier: Modifier = Modifier,
    parserSettings: ParserSettings,
    onAction: (SettingsAction) -> Unit,
) {
    VerticallyScrollableContainer(modifier = modifier) {
        Column {
            Group(text = stringResource("settings.parser.group.general")) {
                CheckboxSettingsRow(
                    text = stringResource("settings.parser.strict.mode"),
                    infoText = stringResource("settings.parser.strict.mode.description"),
                    checked = parserSettings.strictMode,
                    onCheckedChange = { onAction(ParserStrictMode(it)) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun ParserSettingsPreview() = ProjectPreviewTheme {
    TiamatPreview(ParserSettingsScreen)
}