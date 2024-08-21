package io.github.composegears.valkyrie.ui.domain.validation

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

data class InputState(
  val text: String = "",
  val enabled: Boolean = true,
  val validationResult: ValidationResult = ValidationResult.Success,
)
