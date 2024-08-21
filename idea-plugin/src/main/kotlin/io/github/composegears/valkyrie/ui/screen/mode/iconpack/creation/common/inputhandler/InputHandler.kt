package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.inputhandler

import io.github.composegears.valkyrie.ui.domain.validation.IconPackValidationUseCase
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.domain.validation.PackageValidationUseCase
import io.github.composegears.valkyrie.ui.domain.validation.ValidationResult
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.NestedPack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface InputHandler {
  val state: StateFlow<InputFieldState>

  suspend fun handleInput(change: InputChange)

  fun addNestedPack()
  fun removeNestedPack(nestedPack: NestedPack)
}

abstract class BasicInputHandler(initialState: InputFieldState) : InputHandler {

  private val packageValidationUseCase = PackageValidationUseCase()
  private val iconPackValidationUseCase = IconPackValidationUseCase()

  private val _state = MutableStateFlow(initialState)
  override val state: StateFlow<InputFieldState> = _state.asStateFlow()

  override fun addNestedPack() {
    _state.updateState {
      copy(
        nestedPacks = nestedPacks + NestedPack(
          id = nestedPacks.size.toString(),
          inputFieldState = InputState(
            text = "",
            validationResult = ValidationResult.None,
            enabled = true,
          ),
        ),
      )
    }
  }

  override fun removeNestedPack(nestedPack: NestedPack) {
    _state.updateState {
      copy(nestedPacks = nestedPacks.filterNot { it.id == nestedPack.id })
    }
  }

  override suspend fun handleInput(change: InputChange) {
    when (change) {
      is InputChange.IconPackName -> {
        _state.updateState {
          copy(
            iconPackName = iconPackName.copy(
              text = change.text,
              validationResult = ValidationResult.Success,
            ),
          )
        }
      }
      is InputChange.PackageName -> {
        _state.updateState {
          copy(
            packageName = packageName.copy(
              text = change.text,
              validationResult = ValidationResult.Success,
            ),
          )
        }
      }
      is InputChange.NestedPackName -> {
        _state.updateState {
          copy(
            nestedPacks = nestedPacks.map {
              if (it.id == change.id) {
                it.copy(inputFieldState = it.inputFieldState.copy(text = change.text))
              } else {
                it
              }
            },
          )
        }
      }
    }
    validate()
  }

  fun updateState(inputFieldState: InputFieldState) {
    _state.updateState { inputFieldState }
  }

  private suspend fun validate() {
    val inputFieldState = _state.value
    val packageResult = packageValidationUseCase(inputFieldState.packageName.text)
    val iconPackResult = iconPackValidationUseCase(inputFieldState.iconPackName.text)
    val nestedPackResults = inputFieldState.nestedPacks.map {
      iconPackValidationUseCase(it.inputFieldState.text)
    }

    _state.updateState {
      copy(
        iconPackName = iconPackName.copy(validationResult = iconPackResult),
        packageName = packageName.copy(validationResult = packageResult),
        nestedPacks = nestedPacks.mapIndexed { index, nestedPack ->
          nestedPack.copy(
            inputFieldState = nestedPack.inputFieldState.copy(validationResult = nestedPackResults[index]),
          )
        },
      )
    }
  }
}
