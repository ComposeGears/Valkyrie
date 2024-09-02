package io.github.composegears.valkyrie.ui.screen.settings.tabs.export

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composegears.tiamat.koin.koinSharedTiamatViewModel
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.dim
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration
import org.koin.compose.koinInject

val ImageVectorExportSettingsScreen by navDestination<Unit> {
    val inMemorySettings = koinInject<InMemorySettings>()
    val settings by inMemorySettings.settings.collectAsState()

    val settingsViewModel = koinSharedTiamatViewModel<SettingsViewModel>()

    ImageVectorExportSettingsUi(
        onAction = settingsViewModel::onAction,
        outputFormat = settings.outputFormat,
        generatePreview = settings.generatePreview,
    )
}

@Composable
private fun ImageVectorExportSettingsUi(
    outputFormat: OutputFormat,
    generatePreview: Boolean,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        VerticalSpacer(16.dp)
        OutputFormatSection(
            outputFormat = outputFormat,
            onAction = onAction,
        )
        VerticalSpacer(16.dp)
        ListItem(
            modifier = Modifier
                .toggleable(
                    value = generatePreview,
                    onValueChange = { onAction(UpdatePreviewGeneration(it)) },
                )
                .padding(horizontal = 8.dp)
                .heightIn(max = 100.dp),
            headlineContent = {
                Text(text = "Include @Preview block")
            },
            supportingContent = {
                Text(
                    text = "Deprecated option, please consider to use build-in ImageVector preview feature",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalContentColor.current.dim(),
                )
            },
            trailingContent = {
                Switch(
                    modifier = Modifier.scale(0.9f),
                    checked = generatePreview,
                    onCheckedChange = { onAction(UpdatePreviewGeneration(it)) },
                )
            },
        )
    }
}

@Preview
@Composable
private fun ImageVectorExportSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    ImageVectorExportSettingsUi(
        onAction = {},
        outputFormat = OutputFormat.BackingProperty,
        generatePreview = true,
    )
}
