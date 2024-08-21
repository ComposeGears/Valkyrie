package io.github.composegears.valkyrie.ui.screen.settings

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.settings.InMemorySettings
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
      is UpdatePreviewGeneration -> updateGeneratePreview(settingsAction.generate)
      is UpdateOutputFormat -> updateOutputFormat(settingsAction.outputFormat)
    }
  }

  private fun updateGeneratePreview(generatePreview: Boolean) {
    inMemorySettings.updateGeneratePreview(generatePreview)
  }

  private fun updateOutputFormat(outputFormat: OutputFormat) {
    inMemorySettings.updateOutputFormat(outputFormat)
  }

  fun clearSettings() = inMemorySettings.clear()

  fun resetMode() = inMemorySettings.updateMode(Unspecified)
}
