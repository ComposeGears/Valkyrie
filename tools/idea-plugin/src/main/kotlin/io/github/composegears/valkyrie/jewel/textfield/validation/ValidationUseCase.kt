package io.github.composegears.valkyrie.jewel.textfield.validation

import io.github.composegears.valkyrie.ui.domain.ParamUseCase

interface ValidationUseCase : ParamUseCase<String, ValidationResult>

sealed interface ValidationResult {
    data object None : ValidationResult
    data object Success : ValidationResult
    data class Error(val errorCriteria: ErrorCriteria) : ValidationResult
}

enum class ErrorCriteria {
    EMPTY,
    INCONSISTENT_FORMAT,
    FIRST_LETTER_LOWER_CASE,
}
