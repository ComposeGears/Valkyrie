package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler

import io.github.composegears.valkyrie.jewel.textfield.validation.ValidationResult
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface InputHandler {
    val state: StateFlow<InputFieldState>

    fun handleInput(change: InputChange)

    fun addNestedPack()
    fun removeNestedPack(nestedPack: NestedPack)
}

abstract class BasicInputHandler(initialState: InputFieldState) : InputHandler {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<InputFieldState> = _state.asStateFlow()

    override fun addNestedPack() {
        _state.updateState {
            copy(
                nestedPacks = nestedPacks +
                    NestedPack(
                        id = nestedPacks.size.toString(),
                        inputFieldState = InputState(),
                    ),
            )
        }
    }

    override fun removeNestedPack(nestedPack: NestedPack) {
        _state.updateState {
            copy(nestedPacks = nestedPacks.filterNot { it.id == nestedPack.id })
        }
    }

    override fun handleInput(change: InputChange) {
        when (change) {
            is InputChange.IconPackName -> {
                _state.updateState {
                    copy(iconPackName = iconPackName.copy(text = change.text))
                }
            }
            is InputChange.IconPackNameValidation -> {
                _state.updateState {
                    copy(iconPackName = iconPackName.copy(isValid = change.validationResult is ValidationResult.Success))
                }
            }
            is InputChange.PackageName -> {
                _state.updateState {
                    copy(packageName = packageName.copy(text = change.text))
                }
            }
            is InputChange.PackageNameValidation -> {
                _state.updateState {
                    copy(packageName = packageName.copy(isValid = change.validationResult is ValidationResult.Success))
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
            is InputChange.NestedPackNameValidation -> {
                _state.updateState {
                    copy(
                        nestedPacks = nestedPacks.map {
                            if (it.id == change.id) {
                                it.copy(inputFieldState = it.inputFieldState.copy(isValid = change.validationResult is ValidationResult.Success))
                            } else {
                                it
                            }
                        },
                    )
                }
            }
        }
    }

    fun updateState(inputFieldState: InputFieldState) {
        _state.updateState { inputFieldState }
    }

    fun invalidate(settings: ValkyriesSettings) {
        _state.updateState { provideInputFieldState(settings) }
    }

    abstract fun provideInputFieldState(settings: ValkyriesSettings): InputFieldState
}
