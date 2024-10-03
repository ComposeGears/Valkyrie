package io.github.composegears.valkyrie.ui.screen.settings

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.updateMode
import io.github.composegears.valkyrie.settings.updateOutputFormat
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateAddTrailingComma
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateExplicitMode
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateFlatPackage
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateImageVectorPreview
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateOutputFormat
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration

class SettingsViewModel(
    private val inMemorySettings: InMemorySettings,
) : TiamatViewModel() {

    val settings = inMemorySettings.settings

    fun onAction(settingsAction: SettingsAction) = inMemorySettings.update {
        when (settingsAction) {
            is UpdatePreviewGeneration -> generatePreview = settingsAction.generate
            is UpdateOutputFormat -> updateOutputFormat(settingsAction.outputFormat)
            is UpdateImageVectorPreview -> showImageVectorPreview = settingsAction.enabled
            is UpdateFlatPackage -> flatPackage = settingsAction.useFlatPackage
            is UpdateExplicitMode -> useExplicitMode = settingsAction.useExplicitMode
            is UpdateAddTrailingComma -> addTrailingComma = settingsAction.addTrailingComma
        }
    }

    fun clearSettings() = inMemorySettings.clear()

    fun resetMode() = inMemorySettings.update {
        updateMode(Unspecified)
    }
}
