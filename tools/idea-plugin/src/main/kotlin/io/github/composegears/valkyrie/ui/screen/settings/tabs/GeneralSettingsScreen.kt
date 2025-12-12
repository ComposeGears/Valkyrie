package io.github.composegears.valkyrie.ui.screen.settings.tabs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.editNavStack
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigationFadeInOut
import com.composegears.tiamat.navigation.NavController
import com.composegears.tiamat.navigation.NavDestination.Companion.toNavEntry
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.util.dim
import io.github.composegears.valkyrie.compose.util.disabled
import io.github.composegears.valkyrie.shared.Mode.Editor
import io.github.composegears.valkyrie.shared.Mode.IconPack
import io.github.composegears.valkyrie.shared.Mode.ImageVectorToXml
import io.github.composegears.valkyrie.shared.Mode.Simple
import io.github.composegears.valkyrie.shared.Mode.Unspecified
import io.github.composegears.valkyrie.shared.Mode.WebImport
import io.github.composegears.valkyrie.ui.foundation.InfoItem
import io.github.composegears.valkyrie.ui.foundation.icons.PlayForward
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.rememberCurrentProject
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.screen.settings.GeneralSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel

val GeneralSettingsScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = viewModel<SettingsViewModel>(navController)
    val generalSettings by viewModel.generalSettings.collectAsState()

    var showClearSettingsDialog by rememberMutableState { false }

    GeneralSettingsUi(
        generalSettings = generalSettings,
        onClearSettings = {
            showClearSettingsDialog = true
        },
        onChangeMode = {
            viewModel.resetMode()
            openIntro(navController)
        },
    )

    if (showClearSettingsDialog) {
        ClearSettingsDialog(
            onClear = {
                viewModel.clearSettings()
                showClearSettingsDialog = false

                openIntro(navController)
            },
            onCancel = { showClearSettingsDialog = false },
        )
    }
}

private fun openIntro(navController: NavController) {
    navController.parent?.run {
        editNavStack(transition = navigationFadeInOut()) { _ -> listOf(IntroScreen.toNavEntry()) }
    }
}

@Composable
private fun GeneralSettingsUi(
    generalSettings: GeneralSettings,
    onClearSettings: () -> Unit,
    modifier: Modifier = Modifier,
    onChangeMode: () -> Unit,
) {
    val mode = generalSettings.mode
    val initialMode = remember { mode }
    val currentMode = remember(mode) {
        when (mode) {
            Unspecified -> initialMode
            else -> mode
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        VerticalSpacer(16.dp)
        ListItem(
            modifier = Modifier
                .clickable(
                    onClick = {
                        if (mode != Unspecified) onChangeMode()
                    },
                )
                .padding(horizontal = 8.dp),
            headlineContent = {
                val name = when (currentMode) {
                    Simple -> "Simple"
                    IconPack -> "IconPack"
                    Editor -> "Editor"
                    ImageVectorToXml -> "ImageVector to XML"
                    Unspecified, WebImport -> "Unspecified"
                }
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = "Current mode: $name",
                )
            },
            supportingContent = {
                Text(
                    text = "Will be opened by default after the plugin relaunch",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalContentColor.current.dim(),
                )
            },
            trailingContent = {
                val tint = if (mode == Unspecified) LocalContentColor.current.disabled() else LocalContentColor.current
                CenterVerticalRow(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onChangeMode, enabled = mode != Unspecified)
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = "Change",
                        color = tint,
                    )
                    Icon(
                        imageVector = ValkyrieIcons.PlayForward,
                        tint = tint,
                        contentDescription = null,
                    )
                }
            },
        )
        val currentProject = rememberCurrentProject()

        InfoItem(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = "Import path",
            description = when {
                generalSettings.iconPackDestination.isEmpty() -> "Not specified"
                else -> "~${generalSettings.iconPackDestination.replace(currentProject.path.orEmpty(), "")}"
            },
        )
        VerticalSpacer(16.dp)
        InfoItem(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = "Package",
            description = generalSettings.packageName.ifEmpty { "Not specified" },
        )
        VerticalSpacer(16.dp)
        WeightSpacer()
        SectionTitle(name = "Danger zone")
        TextButton(
            modifier = Modifier.padding(horizontal = 12.dp),
            colors = ButtonDefaults.textButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.error,
            ),
            onClick = onClearSettings,
        ) {
            Text(text = "Clear all plugin settings")
        }
        VerticalSpacer(16.dp)
    }
}

@Composable
private fun SectionTitle(
    name: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
) {
    Text(
        modifier = modifier.padding(paddingValues),
        text = name,
        color = MaterialTheme.colorScheme.onSurfaceVariant.dim(),
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Normal,
        ),
    )
}

@Composable
private fun ClearSettingsDialog(
    onClear: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        shape = MaterialTheme.shapes.extraSmall,
        tonalElevation = 0.dp,
        title = {
            Text("Reset settings?")
        },
        text = {
            Text("This will reset plugin preferences back to their default settings and redirect to the start screen.")
        },
        textContentColor = MaterialTheme.colorScheme.onSurface,
        confirmButton = {
            Button(onClick = onClear) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Text("Cancel")
            }
        },
    )
}

@Preview
@Composable
private fun GeneralSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    GeneralSettingsUi(
        generalSettings = GeneralSettings(
            mode = Simple,
            packageName = "io.github.composegears.valkyrie",
            iconPackDestination = "path/to/import",
        ),
        onChangeMode = {},
        onClearSettings = {},
    )
}
