package io.github.composegears.valkyrie.ui.domain.validation

class PackageValidationUseCase : ValidationUseCase {

    private val packageRegex = "^([A-Za-z][A-Za-z\\d_]*\\.)*[A-Za-z][A-Za-z\\d_]*$".toRegex()

    override suspend fun invoke(params: String): ValidationResult {
        return when {
            params.isEmpty() -> ValidationResult.Error(errorCriteria = ErrorCriteria.EMPTY)
            !params.matches(packageRegex) -> ValidationResult.Error(errorCriteria = ErrorCriteria.INCONSISTENT_FORMAT)
            else -> ValidationResult.Success
        }
    }
}