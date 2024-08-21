package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.PackEditState

sealed interface NewPackModeState {
  data class ChooseExportDirectoryState(
    val iconPackDestination: String,
    val predictedPackage: String,
    val nextAvailable: Boolean,
  ) : NewPackModeState

  data class PickedState(
    val packEditState: PackEditState = PackEditState(),
    val nextAvailable: Boolean = true,
  ) : NewPackModeState
}
