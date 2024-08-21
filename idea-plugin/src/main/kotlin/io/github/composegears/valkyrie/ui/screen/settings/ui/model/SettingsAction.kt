package io.github.composegears.valkyrie.ui.screen.settings.ui.model

import io.github.composegears.valkyrie.generator.imagevector.OutputFormat

sealed interface SettingsAction {
  data class UpdateOutputFormat(val outputFormat: OutputFormat) : SettingsAction
  data class UpdatePreviewGeneration(val generate: Boolean) : SettingsAction
}
