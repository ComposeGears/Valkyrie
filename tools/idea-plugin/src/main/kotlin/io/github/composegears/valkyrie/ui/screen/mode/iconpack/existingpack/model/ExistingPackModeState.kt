package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState

sealed interface ExistingPackModeState {
    data object ChooserState : ExistingPackModeState

    data class ExistingPackEditState(
        val inputFieldState: InputFieldState = InputFieldState.Empty,
        val importDirectory: String = "",
    ) : ExistingPackModeState
}
