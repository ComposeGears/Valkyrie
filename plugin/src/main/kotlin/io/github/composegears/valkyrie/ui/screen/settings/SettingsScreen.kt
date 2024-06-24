package io.github.composegears.valkyrie.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen

val SettingsScreen by navDestination<Unit> {
    val navController = navController()
    val settingsViewModel = koinTiamatViewModel<SettingsViewModel>()

    val settings by settingsViewModel.settings.collectAsState()

    var showClearSettingsDialog by remember { mutableStateOf(false) }

    SettingsUI(
        settings = settings,
        onGeneratePreviewChanged = settingsViewModel::updateGeneratePreview,
        onBack = navController::back,
        onClearSettings = {
            showClearSettingsDialog = true
        }
    )

    if (showClearSettingsDialog) {
        ClearSettingsDialog(
            onClear = {
                settingsViewModel.clearSettings()
                showClearSettingsDialog = false
                navController.replace(IntroScreen)
            },
            onCancel = { showClearSettingsDialog = false }
        )
    }
}

@Composable
private fun SettingsUI(
    settings: ValkyriesSettings,
    onGeneratePreviewChanged: (Boolean) -> Unit,
    onClearSettings: () -> Unit,
    onBack: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            IconButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Close"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(
                name = "Generation settings",
                paddingValues = PaddingValues(start = 16.dp, top = 0.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .toggleable(
                        value = settings.generatePreview,
                        onValueChange = onGeneratePreviewChanged
                    )
                    .padding(start = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme.colorScheme.onSurface
                    ),
                    checked = settings.generatePreview,
                    onCheckedChange = onGeneratePreviewChanged
                )
                Text(
                    text = "Generate Preview",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            SectionTitle(name = "Danger zone")
            TextButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                onClick = onClearSettings
            ) {
                Text(text = "Clear plugin settings")
            }
        }
    }
}

@Composable
private fun ClearSettingsDialog(
    onClear: () -> Unit,
    onCancel: () -> Unit
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
        }
    )
}

@Composable
private fun SectionTitle(
    name: String,
    paddingValues: PaddingValues = PaddingValues(start = 16.dp, top = 16.dp, bottom = 8.dp)
) {
    Text(
        modifier = Modifier.padding(paddingValues),
        text = name,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodySmall
    )
}

