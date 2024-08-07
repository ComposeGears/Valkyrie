package io.github.composegears.valkyrie.ui.screen.settings

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.domain.model.Mode.IconPack
import io.github.composegears.valkyrie.ui.domain.model.Mode.Simple
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.screen.settings.ui.OutputFormatSection
import io.github.composegears.valkyrie.ui.screen.settings.ui.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.ui.model.SettingsAction.UpdatePreviewGeneration

val SettingsScreen by navDestination<Unit> {
    val navController = navController()
    val settingsViewModel = koinTiamatViewModel<SettingsViewModel>()

    val settings by settingsViewModel.settings.collectAsState()

    var showClearSettingsDialog by rememberMutableState { false }

    SettingsUI(
        settings = settings,
        onChangeMode = {
            settingsViewModel.resetMode()
            navController.editBackStack { clear() }
            navController.replace(IntroScreen)
        },
        onAction = settingsViewModel::onAction,
        onBack = {
            navController.back(transition = navigationSlideInOut(false))
        },
        onClearSettings = {
            showClearSettingsDialog = true
        },
    )

    if (showClearSettingsDialog) {
        ClearSettingsDialog(
            onClear = {
                settingsViewModel.clearSettings()
                showClearSettingsDialog = false
                navController.editBackStack { clear() }
                navController.replace(IntroScreen)
            },
            onCancel = { showClearSettingsDialog = false },
        )
    }
}

@Composable
private fun SettingsUI(
    settings: ValkyriesSettings,
    onAction: (SettingsAction) -> Unit,
    onChangeMode: () -> Unit,
    onClearSettings: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle("Settings")
        }
        VerticalSpacer(16.dp)
        val modeName = when (settings.mode) {
            Simple -> "Simple"
            IconPack -> "Icon Pack"
            Unspecified -> "Unspecified"
        }
        SectionTitle(
            name = "Plugin mode: $modeName",
            paddingValues = PaddingValues(start = 24.dp),
        )
        TextButton(
            modifier = Modifier.padding(start = 12.dp),
            onClick = onChangeMode,
        ) {
            Text(
                text = "Change mode",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        SectionTitle(name = "ImageVector export settings")
        VerticalSpacer(8.dp)
        OutputFormatSection(
            outputFormat = settings.outputFormat,
            onAction = onAction,
        )
        VerticalSpacer(8.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .toggleable(
                    value = settings.generatePreview,
                    onValueChange = { onAction(UpdatePreviewGeneration(it)) },
                )
                .padding(start = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Include @Preview",
                style = MaterialTheme.typography.bodyMedium,
            )
            Switch(
                modifier = Modifier.scale(0.9f),
                checked = settings.generatePreview,
                onCheckedChange = { onAction(UpdatePreviewGeneration(it)) },
            )
        }
        SectionTitle(name = "Danger zone")
        TextButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = ButtonDefaults.textButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.error,
            ),
            onClick = onClearSettings,
        ) {
            Text(text = "Clear all plugin settings")
        }
    }
}

@Composable
private fun ClearSettingsDialog(
    onClear: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text("Clear settings?")
        },
        text = {
            Text("It will remove all plugin data and redirect to Intro screen.")
        },
        textContentColor = MaterialTheme.colorScheme.onSurface,
        confirmButton = {
            TextButton(onClick = onClear) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        },
    )
}

@Composable
private fun SectionTitle(
    name: String,
    paddingValues: PaddingValues = PaddingValues(start = 24.dp, top = 32.dp),
) {
    Text(
        modifier = Modifier
            .padding(paddingValues)
            .height(40.dp),
        text = name,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Preview
@Composable
private fun SettingsScreenPreview() = PreviewTheme {
    SettingsUI(
        settings = ValkyriesSettings(
            mode = Simple,
            packageName = "",
            iconPackName = "",
            iconPackDestination = "",

            nestedPacks = emptyList(),

            outputFormat = OutputFormat.BackingProperty,
            generatePreview = false,
        ),
        onClearSettings = {},
        onChangeMode = {},
        onBack = {},
        onAction = {},
    )
}
