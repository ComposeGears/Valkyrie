package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.DirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState

sealed interface NewPackModeState {
    data class ChooseDestinationDirectoryState(
        val directoryState: DirectoryState = DirectoryState(),
    ) : NewPackModeState

    data class PickedState(
        val inputFieldState: InputFieldState = InputFieldState(
            iconPackName = InputState(),
            packageName = InputState(),
            nestedPacks = emptyList(),
        ),
    ) : NewPackModeState
}
