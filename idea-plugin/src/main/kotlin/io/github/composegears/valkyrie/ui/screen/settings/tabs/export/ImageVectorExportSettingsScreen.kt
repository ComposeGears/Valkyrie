package io.github.composegears.valkyrie.ui.screen.settings.tabs.export

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
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
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.dim
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration

val ImageVectorExportSettingsScreen by navDestination<Unit> {
    val viewModel = koinSharedTiamatViewModel<SettingsViewModel>()
    val settings by viewModel.settings.collectAsState()

    ImageVectorExportSettingsUi(
        onAction = viewModel::onAction,
        outputFormat = settings.outputFormat,
        generatePreview = settings.generatePreview,
        useFlatPackage = settings.flatPackage,
    )
}

@Composable
private fun ImageVectorExportSettingsUi(
    outputFormat: OutputFormat,
    generatePreview: Boolean,
    useFlatPackage: Boolean,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        VerticalSpacer(16.dp)
        OutputFormatSection(
            outputFormat = outputFormat,
            onAction = onAction,
        )
        VerticalSpacer(16.dp)
        SwitchOption(
            title = "Include @Preview block",
            description = "Deprecated option, please consider to use build-in ImageVector preview feature",
            checked = generatePreview,
            onCheckedChange = { onAction(UpdatePreviewGeneration(it)) },
        )
        SwitchOption(
            title = "Use flat package",
            description = "Export all ImageVector icons into a single package without dividing by nested pack folders",
            checked = useFlatPackage,
            onCheckedChange = { onAction(SettingsAction.UpdateFlatPackage(it)) },
        )
        VerticalSpacer(16.dp)
    }
}

@Composable
private fun SwitchOption(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    ListItem(
        modifier = Modifier
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
            )
            .padding(horizontal = 8.dp)
            .heightIn(max = 100.dp),
        headlineContent = {
            Text(text = title)
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = LocalContentColor.current.dim(),
            )
        },
        trailingContent = {
            Switch(
                modifier = Modifier.scale(0.9f),
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        },
    )
}

@Preview
@Composable
private fun ImageVectorExportSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    ImageVectorExportSettingsUi(
        onAction = {},
        outputFormat = OutputFormat.BackingProperty,
        generatePreview = false,
        useFlatPackage = true,
    )
}
