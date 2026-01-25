package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.PackEditState

sealed interface NewPackModeState {
    data class ChooseDestinationDirectoryState(
        val iconPackDestination: String,
        val predictedPackage: String,
        val nextAvailable: Boolean,
    ) : NewPackModeState

    data class PickedState(
        val packEditState: PackEditState = PackEditState(),
        val useMaterialPack: Boolean = false,
        val nextAvailable: Boolean = true,
    ) : NewPackModeState
}
