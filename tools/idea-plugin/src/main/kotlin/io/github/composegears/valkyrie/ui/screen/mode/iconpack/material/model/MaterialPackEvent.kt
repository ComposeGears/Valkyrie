package io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model

sealed interface MaterialPackEvent {
    data object FinishSetup : MaterialPackEvent
}
