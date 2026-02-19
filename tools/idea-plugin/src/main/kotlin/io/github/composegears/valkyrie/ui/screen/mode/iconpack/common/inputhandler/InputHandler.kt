package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.inputhandler

import io.github.composegears.valkyrie.jewel.textfield.validation.ValidationResult
import io.github.composegears.valkyrie.settings.ValkyriesSettings
import io.github.composegears.valkyrie.ui.extension.updateState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import io.github.composegears.valkyrie.util.extension.Uuid
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
                nestedPacks = nestedPacks + NestedPack(
                    id = Uuid.random(),
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
        _state.updateState { applyChange(change) }
    }

    fun setState(inputFieldState: InputFieldState) {
        _state.value = inputFieldState
    }

    fun invalidate(settings: ValkyriesSettings) {
        _state.updateState { invalidateInputFieldState(this, settings) }
    }

    protected open fun invalidateInputFieldState(
        currentState: InputFieldState,
        settings: ValkyriesSettings,
    ): InputFieldState = currentState
}

private fun InputFieldState.applyChange(change: InputChange): InputFieldState = when (change) {
    is InputChange.IconPackName ->
        copy(iconPackName = iconPackName.updateText(change.text))
    is InputChange.IconPackNameValidation ->
        copy(iconPackName = iconPackName.updateValidation(change.validationResult))
    is InputChange.PackageName ->
        copy(packageName = packageName.updateText(change.text))
    is InputChange.PackageNameValidation ->
        copy(packageName = packageName.updateValidation(change.validationResult))
    is InputChange.NestedPackName ->
        copy(nestedPacks = nestedPacks.updateText(change.id, change.text))
    is InputChange.NestedPackNameValidation ->
        copy(nestedPacks = nestedPacks.updateValidation(change.id, change.validationResult))
}

private fun InputState.updateText(text: String): InputState = copy(text = text)

private fun InputState.updateValidation(result: ValidationResult): InputState = copy(isValid = result is ValidationResult.Success)

private fun List<NestedPack>.updateText(id: String, text: String): List<NestedPack> = updateById(id) { copy(text = text) }

private fun List<NestedPack>.updateValidation(id: String, result: ValidationResult): List<NestedPack> = updateById(id) { copy(isValid = result is ValidationResult.Success) }

private inline fun List<NestedPack>.updateById(
    id: String,
    transform: InputState.() -> InputState,
): List<NestedPack> = map { pack ->
    if (pack.id == id) {
        pack.copy(inputFieldState = pack.inputFieldState.transform())
    } else {
        pack
    }
}
