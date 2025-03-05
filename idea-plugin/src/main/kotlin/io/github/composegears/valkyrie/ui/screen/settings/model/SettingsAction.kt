package io.github.composegears.valkyrie.ui.screen.settings.model

import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.ui.domain.model.PreviewType

sealed interface SettingsAction {
    data class UpdateOutputFormat(val outputFormat: OutputFormat) : SettingsAction
    data class UpdateFlatPackage(val useFlatPackage: Boolean) : SettingsAction
    data class UpdateExplicitMode(val useExplicitMode: Boolean) : SettingsAction
    data class UpdateAddTrailingComma(val addTrailingComma: Boolean) : SettingsAction
    data class UpdatePreviewGeneration(val generate: Boolean) : SettingsAction
    data class UpdatePreviewAnnotationType(val annotationType: PreviewAnnotationType) : SettingsAction
    data class UpdateIndentSize(val indent: Int) : SettingsAction

    data class UpdateImageVectorPreview(val enabled: Boolean) : SettingsAction
    data class UpdatePreviewType(val previewType: PreviewType) : SettingsAction
}
