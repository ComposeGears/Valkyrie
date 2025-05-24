package io.github.composegears.valkyrie.ui.screen.settings.tabs.export

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.rememberSharedViewModel
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.ExportSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateAddTrailingComma
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateExplicitMode
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateFlatPackage
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateIndentSize
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewAnnotationType
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateUseComposeColors
import io.github.composegears.valkyrie.ui.screen.settings.tabs.export.ui.SwitchOption
import io.github.composegears.valkyrie.util.stringResource

val ImageVectorExportSettingsScreen by navDestination<Unit> {
    val viewModel = rememberSharedViewModel(provider = ::SettingsViewModel)
    val exportSettings by viewModel.exportSettings.collectAsState()

    ImageVectorExportSettingsUi(
        onAction = viewModel::onAction,
        exportSettings = exportSettings,
    )
}

@Composable
private fun ImageVectorExportSettingsUi(
    exportSettings: ExportSettings,
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
            outputFormat = exportSettings.outputFormat,
            onAction = onAction,
        )
        VerticalSpacer(16.dp)
        SwitchOption(
            title = "Use flat package",
            description = "Export all ImageVector icons into a single package without dividing by nested pack folders",
            checked = exportSettings.useFlatPackage,
            onCheckedChange = { onAction(UpdateFlatPackage(it)) },
        )
        SwitchOption(
            title = stringResource("settings.export.composecolor"),
            description = stringResource("settings.export.composecolor.description"),
            checked = exportSettings.useComposeColors,
            onCheckedChange = { onAction(UpdateUseComposeColors(it)) },
        )
        SwitchOption(
            title = "Handle Kotlin explicit mode",
            description = "Generate ImageVector icons and IconPack with explicit 'public' modifier",
            checked = exportSettings.useExplicitMode,
            onCheckedChange = { onAction(UpdateExplicitMode(it)) },
        )
        SwitchOption(
            title = "Add trailing comma",
            description = "Insert a comma after the last element of ImageVector.Builder block and path params",
            checked = exportSettings.addTrailingComma,
            onCheckedChange = { onAction(UpdateAddTrailingComma(it)) },
        )
        PreviewAnnotationSection(
            generatePreview = exportSettings.generatePreview,
            previewAnnotationType = exportSettings.previewAnnotationType,
            onGeneratePreviewChange = { onAction(UpdatePreviewGeneration(it)) },
            onAnnotationTypeChange = { onAction(UpdatePreviewAnnotationType(it)) },
        )
        IndentSizeSection(
            indent = exportSettings.indentSize,
            onValueChange = { onAction(UpdateIndentSize(it)) },
        )
        VerticalSpacer(16.dp)
    }
}

@Preview
@Composable
private fun ImageVectorExportSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    ImageVectorExportSettingsUi(
        onAction = {},
        exportSettings = ExportSettings(
            outputFormat = OutputFormat.BackingProperty,
            useComposeColors = true,
            generatePreview = true,
            useFlatPackage = true,
            useExplicitMode = false,
            addTrailingComma = false,
            indentSize = 4,
            previewAnnotationType = PreviewAnnotationType.Jetbrains,
        ),
    )
}
