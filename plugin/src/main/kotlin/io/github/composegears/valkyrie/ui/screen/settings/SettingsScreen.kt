package io.github.composegears.valkyrie.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.settings.ValkyriesSettings

val SettingsScreen by navDestination<Unit> {
    val navController = navController()
    val settingsViewModel = koinTiamatViewModel<SettingsViewModel>()

    val settings by settingsViewModel.settings.collectAsState()

    SettingsUI(
        settings = settings,
        onGeneratePreviewChanged = settingsViewModel::updateGeneratePreview,
        onBack = navController::back
    )
}


@Composable
private fun SettingsUI(
    settings: ValkyriesSettings,
    onGeneratePreviewChanged: (Boolean) -> Unit,
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
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Export settings",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))

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
        }
    }
}

