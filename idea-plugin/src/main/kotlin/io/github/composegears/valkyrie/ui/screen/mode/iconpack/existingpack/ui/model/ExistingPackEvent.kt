package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model

sealed interface ExistingPackEvent {
    data class PreviewIconPackObject(val code: String) : ExistingPackEvent
    data object OnSettingsUpdated : ExistingPackEvent
}
