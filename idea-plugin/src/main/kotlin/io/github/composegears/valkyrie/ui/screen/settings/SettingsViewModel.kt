package io.github.composegears.valkyrie.ui.screen.settings

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.updateOutputFormat
import io.github.composegears.valkyrie.ui.di.DI
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateAddTrailingComma
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateExplicitMode
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateFlatPackage
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateImageVectorPreview
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateIndentSize
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateOutputFormat
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration

class SettingsViewModel : TiamatViewModel() {

    private val inMemorySettings by DI.core.inMemorySettings

    val settings = inMemorySettings.settings

    fun onAction(settingsAction: SettingsAction) = inMemorySettings.update {
        when (settingsAction) {
            is UpdatePreviewGeneration -> generatePreview = settingsAction.generate
            is UpdateOutputFormat -> updateOutputFormat(settingsAction.outputFormat)
            is UpdateImageVectorPreview -> showImageVectorPreview = settingsAction.enabled
            is UpdateFlatPackage -> flatPackage = settingsAction.useFlatPackage
            is UpdateExplicitMode -> useExplicitMode = settingsAction.useExplicitMode
            is UpdateAddTrailingComma -> addTrailingComma = settingsAction.addTrailingComma
            is UpdateIndentSize -> indentSize = settingsAction.indent
            is SettingsAction.UpdatePreviewType -> previewType = settingsAction.previewType
        }
    }

    fun clearSettings() = inMemorySettings.clear()

    fun resetMode() = inMemorySettings.update {
        mode = Unspecified
    }
}
