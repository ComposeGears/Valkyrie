package io.github.composegears.valkyrie.ui.screen.settings.tabs.generator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.settings.GeneratorSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateAddTrailingComma
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateExplicitMode
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateFlatPackage
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateIndentSize
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewAnnotationType
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateUseComposeColors
import io.github.composegears.valkyrie.ui.screen.settings.tabs.generator.ui.SwitchOption
import io.github.composegears.valkyrie.uikit.tooling.PreviewTheme
import io.github.composegears.valkyrie.util.stringResource

val GeneratorSettingsScreen by navDestination<Unit> {
    val viewModel = viewModel<SettingsViewModel>(navController())
    val generatorSettings by viewModel.generatorSettings.collectAsState()

    GeneratorSettingsUi(
        onAction = viewModel::onAction,
        generatorSettings = generatorSettings,
    )
}

@Composable
private fun GeneratorSettingsUi(
    generatorSettings: GeneratorSettings,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(16.dp)
        OutputFormatSection(
            outputFormat = generatorSettings.outputFormat,
            onAction = onAction,
        )
        Spacer(16.dp)
        SwitchOption(
            title = stringResource("settings.generator.flat.package"),
            description = stringResource("settings.generator.flat.package.description"),
            checked = generatorSettings.useFlatPackage,
            onCheckedChange = { onAction(UpdateFlatPackage(it)) },
        )
        SwitchOption(
            title = stringResource("settings.generator.composecolor"),
            description = stringResource("settings.generator.composecolor.description"),
            checked = generatorSettings.useComposeColors,
            onCheckedChange = { onAction(UpdateUseComposeColors(it)) },
        )
        SwitchOption(
            title = stringResource("settings.generator.explicit.mode"),
            description = stringResource("settings.generator.explicit.mode.description"),
            checked = generatorSettings.useExplicitMode,
            onCheckedChange = { onAction(UpdateExplicitMode(it)) },
        )
        SwitchOption(
            title = stringResource("settings.generator.trailing.comma"),
            description = stringResource("settings.generator.trailing.comma.description"),
            checked = generatorSettings.addTrailingComma,
            onCheckedChange = { onAction(UpdateAddTrailingComma(it)) },
        )
        PreviewAnnotationSection(
            generatePreview = generatorSettings.generatePreview,
            previewAnnotationType = generatorSettings.previewAnnotationType,
            onGeneratePreviewChange = { onAction(UpdatePreviewGeneration(it)) },
            onAnnotationTypeChange = { onAction(UpdatePreviewAnnotationType(it)) },
        )
        IndentSizeSection(
            indent = generatorSettings.indentSize,
            onValueChange = { onAction(UpdateIndentSize(it)) },
        )
        Spacer(16.dp)
    }
}

@Preview
@Composable
internal fun GeneratorSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    GeneratorSettingsUi(
        onAction = {},
        generatorSettings = GeneratorSettings(
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
