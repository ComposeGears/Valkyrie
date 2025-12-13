package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.PackEditState

sealed interface ExistingPackModeState {
    data object ChooserState : ExistingPackModeState

    data class ExistingPackEditState(
        val packEditState: PackEditState = PackEditState(),
        val importDirectory: String = "",
        val nextAvailable: Boolean = true,
    ) : ExistingPackModeState
}
