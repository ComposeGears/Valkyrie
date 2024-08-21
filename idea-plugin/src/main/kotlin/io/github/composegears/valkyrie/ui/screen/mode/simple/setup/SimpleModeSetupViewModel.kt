package io.github.composegears.valkyrie.ui.screen.mode.simple.setup

import com.composegears.tiamat.TiamatViewModel
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.domain.validation.PackageValidationUseCase
import io.github.composegears.valkyrie.ui.domain.validation.ValidationResult
import io.github.composegears.valkyrie.ui.extension.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SimpleModeSetupViewModel(
  private val inMemorySettings: InMemorySettings,
) : TiamatViewModel() {

  private val inputHandler = SimpleModeInputHandler(inMemorySettings)

  private val valkyriesSettings: ValkyriesSettings
    get() = inMemorySettings.current

  private val _state = MutableStateFlow(
    SimpleModeSetupState(
      packageName = InputState(text = valkyriesSettings.packageName),
      nextAvailable = false,
    ),
  )
  val state = _state.asStateFlow()

  init {
    viewModelScope.launch {
      inputHandler.state.collect {
        _state.updateState {
          copy(
            packageName = it.packageName,
            nextAvailable = it.noErrors(),
          )
        }
      }
    }
  }

  fun onValueChange(change: SimpleModeInputChange) = viewModelScope.launch {
    inputHandler.handleInput(change)
  }

  fun saveSettings() {
    val setupState = state.value
    inMemorySettings.updatePackageName(setupState.packageName.text)
    inMemorySettings.updateMode(Mode.Simple)
  }
}

sealed interface SimpleModeInputChange {
  data class PackageName(val text: String) : SimpleModeInputChange
}

data class SimpleModeSetupState(
  val packageName: InputState = InputState(),
  val nextAvailable: Boolean = false,
)

private class SimpleModeInputHandler(private val inMemorySettings: InMemorySettings) {

  private val valkyriesSettings: ValkyriesSettings
    get() = inMemorySettings.current

  private val packageValidationUseCase = PackageValidationUseCase()

  private val _state = MutableStateFlow(
    InputFieldState(packageName = InputState(text = valkyriesSettings.packageName)),
  )
  val state = _state.asStateFlow()

  suspend fun handleInput(change: SimpleModeInputChange) {
    when (change) {
      is SimpleModeInputChange.PackageName -> {
        _state.updateState {
          copy(
            packageName = packageName.copy(
              text = change.text,
              validationResult = ValidationResult.Success,
            ),
          )
        }
      }
    }
    validate()
  }

  private suspend fun validate() {
    val inputFieldState = _state.value
    val packageResult = packageValidationUseCase(inputFieldState.packageName.text)

    _state.updateState {
      copy(packageName = packageName.copy(validationResult = packageResult))
    }
  }

  data class InputFieldState(val packageName: InputState) {
    fun noErrors() = packageName.validationResult !is ValidationResult.Error
  }
}
