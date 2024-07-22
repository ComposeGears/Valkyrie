package io.github.composegears.valkyrie.ui.domain.validation

class IconPackValidationUseCase : ValidationUseCase {

    private val iconPackRegex = "^[A-Za-z]*$".toRegex()

    override suspend fun invoke(params: String): ValidationResult {
        return when {
            params.isEmpty() -> ValidationResult.Error(errorCriteria = ErrorCriteria.EMPTY)
            params.first().isLowerCase() -> ValidationResult.Error(errorCriteria = ErrorCriteria.FIRST_LETTER_LOWER_CASE)
            !params.matches(iconPackRegex) -> ValidationResult.Error(errorCriteria = ErrorCriteria.INCONSISTENT_FORMAT)
            else -> ValidationResult.Success
        }
    }
}
