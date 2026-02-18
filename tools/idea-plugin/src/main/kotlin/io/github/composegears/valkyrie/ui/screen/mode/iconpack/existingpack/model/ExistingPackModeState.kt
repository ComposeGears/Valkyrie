package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState

sealed interface ExistingPackModeState {
    data object ChooserState : ExistingPackModeState

    data class ExistingPackEditState(
        val inputFieldState: InputFieldState = InputFieldState(
            iconPackName = InputState(),
            packageName = InputState(),
            nestedPacks = emptyList(),
        ),
        val importDirectory: String = "",
    ) : ExistingPackModeState
}
