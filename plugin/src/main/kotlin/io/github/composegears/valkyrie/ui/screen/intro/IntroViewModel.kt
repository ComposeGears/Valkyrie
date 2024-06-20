package io.github.composegears.valkyrie.ui.screen.intro

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.ValkyrieSettings
import io.github.composegears.valkyrie.ui.screen.intro.InputChange.IconPackName
import io.github.composegears.valkyrie.ui.screen.intro.InputChange.PackageName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IntroViewModel : TiamatViewModel() {
   private val settingsService = ValkyrieSettings.instance

    private val inputHandler = InputHandler()

    private val _introState = MutableStateFlow(IntroState())
    val introState = _introState.asStateFlow()

    init {
        viewModelScope.launch {
            inputHandler.state.collect { inputFieldState ->
                _introState.updateState {
                    copy(
                        inputFieldState = inputFieldState,
                        nextAvailable = inputFieldState.noErrors()
                    )
                }
            }
        }
    }

    fun onValueChange(change: InputChange) = viewModelScope.launch {
        inputHandler.handleInput(change)
    }

    fun saveSettings() {
        val inputFieldState = introState.value.inputFieldState
        settingsService.isFirstStart = false
        settingsService.packageName = inputFieldState.packageName.text
        settingsService.iconPackName = inputFieldState.iconPackName.text
    }
}

class InputHandler {
    private val packageValidationUseCase = PackageValidationUseCase()
    private val iconPackValidationUseCase = IconPackValidationUseCase()

    private val _state = MutableStateFlow(InputFieldState())
    val state = _state.asStateFlow()

    suspend fun handleInput(change: InputChange) {
        when (change) {
            is IconPackName -> {
                _state.updateState {
                    copy(
                        iconPackName = iconPackName.copy(
                            text = change.text,
                            validationResult = ValidationResult.Success
                        )
                    )
                }
            }
            is PackageName -> {
                _state.updateState {
                    copy(
                        packageName = packageName.copy(
                            text = change.text,
                            validationResult = ValidationResult.Success
                        )
                    )
                }
            }
        }
        validate()
    }

    private suspend fun validate() {
        val inputFieldState = _state.value
        val packageResult = packageValidationUseCase(inputFieldState.packageName.text)
        val iconPackResult = iconPackValidationUseCase(inputFieldState.iconPackName.text)

        _state.updateState {
            copy(
                iconPackName = iconPackName.copy(validationResult = iconPackResult),
                packageName = packageName.copy(validationResult = packageResult)
            )
        }
    }
}

fun <T> MutableStateFlow<T>.updateState(reduce: T.() -> T) {
    update(reduce)
}

sealed interface InputChange {
    data class PackageName(val text: String) : InputChange
    data class IconPackName(val text: String) : InputChange
}

data class InputFieldState(
    val iconPackName: InputState = InputState(text = "ValkyrieIcons"),
    val packageName: InputState = InputState(text = "io.github.composegears.valkyrie"),
) {
    fun noErrors() = iconPackName.validationResult !is ValidationResult.Error &&
            packageName.validationResult !is ValidationResult.Error
}

data class IntroState(
    val nextAvailable: Boolean = true,
    val inputFieldState: InputFieldState = InputFieldState()
)

data class InputState(
    val text: String,
    val validationResult: ValidationResult = ValidationResult.Success
)

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


class IconPackValidationUseCase : ValidationUseCase {

    private val iconPackRegex = "^[A-Za-z]*$".toRegex()

    override suspend fun invoke(params: String): ValidationResult {
        return when {
            params.isEmpty() -> ValidationResult.Error(errorCriteria = ErrorCriteria.EMPTY)
            !params.matches(iconPackRegex) -> ValidationResult.Error(errorCriteria = ErrorCriteria.INCONSISTENT_FORMAT)
            else -> ValidationResult.Success
        }
    }
}


interface ParamUseCase<Params : Any, Success : Any> {
    suspend operator fun invoke(params: Params): Success
}

interface ValidationUseCase : ParamUseCase<String, ValidationResult>

sealed class ValidationResult {

    data object Success : ValidationResult()

    data class Error(
        val errorCriteria: ErrorCriteria
    ) : ValidationResult()
}

enum class ErrorCriteria {
    EMPTY,
    INCONSISTENT_FORMAT
}