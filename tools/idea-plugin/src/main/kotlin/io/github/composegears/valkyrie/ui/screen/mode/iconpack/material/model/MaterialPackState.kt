package io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.DirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState

sealed interface MaterialPackState {
    data class ChooseDestinationDirectoryState(
        val directoryState: DirectoryState = DirectoryState(),
    ) : MaterialPackState

    data class PickedState(
        val inputFieldState: InputFieldState,
        val flatPackageStructure: Boolean = false,
    ) : MaterialPackState
}
