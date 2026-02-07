package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model

import io.github.composegears.valkyrie.jewel.textfield.validation.ValidationResult

sealed interface InputChange {
    data class PackageName(val text: String) : InputChange
    data class IconPackName(val text: String) : InputChange
    data class NestedPackName(val id: String, val text: String) : InputChange
    data class PackageNameValidation(val validationResult: ValidationResult) : InputChange
    data class IconPackNameValidation(val validationResult: ValidationResult) : InputChange
    data class NestedPackNameValidation(val id: String, val validationResult: ValidationResult) : InputChange
}
