package io.github.composegears.valkyrie.ui.screen.settings.model

import io.github.composegears.valkyrie.generator.imagevector.OutputFormat

sealed interface SettingsAction {
    data class UpdateOutputFormat(val outputFormat: OutputFormat) : SettingsAction
    data class UpdatePreviewGeneration(val generate: Boolean) : SettingsAction
    data class UpdateFlatPackage(val useFlatPackage: Boolean) : SettingsAction
    data class UpdateImageVectorPreview(val enabled: Boolean) : SettingsAction
}
