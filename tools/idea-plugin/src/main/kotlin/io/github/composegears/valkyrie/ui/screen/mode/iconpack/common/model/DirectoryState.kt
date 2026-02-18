package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model

data class DirectoryState(
    val iconPackDestination: String = "",
    val predictedPackage: String = "",
    val nextAvailable: Boolean = false,
)
