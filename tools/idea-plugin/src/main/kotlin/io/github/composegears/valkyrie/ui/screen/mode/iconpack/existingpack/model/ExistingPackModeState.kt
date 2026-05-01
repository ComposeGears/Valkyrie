package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState

sealed interface ExistingPackModeState {
    data object ChooserState : ExistingPackModeState

    data class ExistingPackEditState(
        val inputFieldState: InputFieldState = InputFieldState.Empty,
        val initialInputFieldState: InputFieldState = InputFieldState.Empty,
        val importDirectory: String = "",
    ) : ExistingPackModeState {

        val isModified: Boolean
            get() = inputFieldState.license.text != initialInputFieldState.license.text ||
                inputFieldState.nestedPacks.map { it.inputFieldState.text } !=
                initialInputFieldState.nestedPacks.map { it.inputFieldState.text }
    }
}
