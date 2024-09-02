package io.github.composegears.valkyrie.ui.screen.settings

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.updateMode
import io.github.composegears.valkyrie.settings.updateOutputFormat
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.screen.settings.ui.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.ui.model.SettingsAction.UpdateOutputFormat
import io.github.composegears.valkyrie.ui.screen.settings.ui.model.SettingsAction.UpdatePreviewGeneration

class SettingsViewModel(
    private val inMemorySettings: InMemorySettings,
) : TiamatViewModel() {

    val settings = inMemorySettings.settings

    fun onAction(settingsAction: SettingsAction) {
        when (settingsAction) {
            is UpdatePreviewGeneration -> inMemorySettings.update {
                generatePreview = settingsAction.generate
            }
            is UpdateOutputFormat -> inMemorySettings.update {
                updateOutputFormat(settingsAction.outputFormat)
            }
            is SettingsAction.ShowImageVectorPreview -> inMemorySettings.update {
                showImageVectorPreview = settingsAction.show
            }
        }
    }

    fun clearSettings() = inMemorySettings.clear()

    fun resetMode() = inMemorySettings.update {
        updateMode(Unspecified)
    }
}
