package io.github.composegears.valkyrie.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.composegears.leviathan.compose.inject
import com.intellij.collaboration.async.mapState
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.settings.updateOutputFormat
import io.github.composegears.valkyrie.settings.updatePreviewAnnotationType
import io.github.composegears.valkyrie.shared.Mode
import io.github.composegears.valkyrie.shared.Mode.Unspecified
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateAddTrailingComma
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateExplicitMode
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateFlatPackage
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateImageVectorPreview
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateIndentSize
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateOutputFormat
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewAnnotationType
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateUseComposeColors

@Suppress("UnstableApiUsage")
class SettingsViewModel : ViewModel() {

    private val inMemorySettings = inject(DI.core.inMemorySettings)

    val generatorSettings = inMemorySettings.settings.mapState {
        GeneratorSettings(
            outputFormat = it.outputFormat,
            useComposeColors = it.useComposeColors,
            generatePreview = it.generatePreview,
            useFlatPackage = it.flatPackage,
            useExplicitMode = it.useExplicitMode,
            addTrailingComma = it.addTrailingComma,
            indentSize = it.indentSize,
            previewAnnotationType = it.previewAnnotationType,
        )
    }

    val generalSettings = inMemorySettings.settings.mapState {
        GeneralSettings(
            mode = it.mode,
            packageName = it.packageName,
            iconPackDestination = it.iconPackDestination,
        )
    }

    val previewSettings = inMemorySettings.settings.mapState {
        PreviewSettings(
            previewType = it.previewType,
            showImageVectorPreview = it.showImageVectorPreview,
        )
    }

    fun onAction(settingsAction: SettingsAction) = inMemorySettings.update {
        when (settingsAction) {
            is UpdatePreviewGeneration -> generatePreview = settingsAction.generate
            is UpdateUseComposeColors -> useComposeColors = settingsAction.useComposeColor
            is UpdateOutputFormat -> updateOutputFormat(settingsAction.outputFormat)
            is UpdateImageVectorPreview -> showImageVectorPreview = settingsAction.enabled
            is UpdateFlatPackage -> flatPackage = settingsAction.useFlatPackage
            is UpdateExplicitMode -> useExplicitMode = settingsAction.useExplicitMode
            is UpdateAddTrailingComma -> addTrailingComma = settingsAction.addTrailingComma
            is UpdateIndentSize -> indentSize = settingsAction.indent
            is SettingsAction.UpdatePreviewType -> previewType = settingsAction.previewType
            is UpdatePreviewAnnotationType -> updatePreviewAnnotationType(settingsAction.annotationType)
        }
    }

    fun clearSettings() = inMemorySettings.clear()

    fun resetMode() = inMemorySettings.update {
        mode = Unspecified
    }
}

data class GeneratorSettings(
    val outputFormat: OutputFormat,
    val useComposeColors: Boolean,
    val useFlatPackage: Boolean,
    val useExplicitMode: Boolean,
    val addTrailingComma: Boolean,
    val indentSize: Int,
    val generatePreview: Boolean,
    val previewAnnotationType: PreviewAnnotationType,
)

data class GeneralSettings(
    val mode: Mode,
    val packageName: String,
    val iconPackDestination: String,
)

data class PreviewSettings(
    val showImageVectorPreview: Boolean,
    val previewType: PreviewType,
)
