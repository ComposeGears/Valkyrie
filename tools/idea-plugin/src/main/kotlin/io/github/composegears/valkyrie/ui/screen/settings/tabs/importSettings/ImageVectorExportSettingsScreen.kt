package io.github.composegears.valkyrie.ui.screen.settings.tabs.importSettings

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.ImportSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateAddTrailingComma
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateExplicitMode
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateFlatPackage
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateIndentSize
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewAnnotationType
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateUseComposeColors
import io.github.composegears.valkyrie.ui.screen.settings.tabs.importSettings.ui.SwitchOption
import io.github.composegears.valkyrie.util.stringResource

val ImageVectorImportSettingsScreen by navDestination<Unit> {
    val viewModel = viewModel<SettingsViewModel>(navController())
    val importSettings by viewModel.importSettings.collectAsState()

    ImageVectorImportSettingsUi(
        onAction = viewModel::onAction,
        importSettings = importSettings,
    )
}

@Composable
private fun ImageVectorImportSettingsUi(
    importSettings: ImportSettings,
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
            outputFormat = importSettings.outputFormat,
            onAction = onAction,
        )
        VerticalSpacer(16.dp)
        SwitchOption(
            title = stringResource("settings.import.flat.package"),
            description = stringResource("settings.import.flat.package.description"),
            checked = importSettings.useFlatPackage,
            onCheckedChange = { onAction(UpdateFlatPackage(it)) },
        )
        SwitchOption(
            title = stringResource("settings.import.composecolor"),
            description = stringResource("settings.import.composecolor.description"),
            checked = importSettings.useComposeColors,
            onCheckedChange = { onAction(UpdateUseComposeColors(it)) },
        )
        SwitchOption(
            title = stringResource("settings.import.explicit.mode"),
            description = stringResource("settings.import.explicit.mode.description"),
            checked = importSettings.useExplicitMode,
            onCheckedChange = { onAction(UpdateExplicitMode(it)) },
        )
        SwitchOption(
            title = stringResource("settings.import.trailing.comma"),
            description = stringResource("settings.import.trailing.comma.description"),
            checked = importSettings.addTrailingComma,
            onCheckedChange = { onAction(UpdateAddTrailingComma(it)) },
        )
        PreviewAnnotationSection(
            generatePreview = importSettings.generatePreview,
            previewAnnotationType = importSettings.previewAnnotationType,
            onGeneratePreviewChange = { onAction(UpdatePreviewGeneration(it)) },
            onAnnotationTypeChange = { onAction(UpdatePreviewAnnotationType(it)) },
        )
        IndentSizeSection(
            indent = importSettings.indentSize,
            onValueChange = { onAction(UpdateIndentSize(it)) },
        )
        VerticalSpacer(16.dp)
    }
}

@Preview
@Composable
private fun ImageVectorImportSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    ImageVectorImportSettingsUi(
        onAction = {},
        importSettings = ImportSettings(
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
