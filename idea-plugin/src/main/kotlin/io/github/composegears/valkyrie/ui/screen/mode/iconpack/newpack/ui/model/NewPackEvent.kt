package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model

sealed interface NewPackEvent {
  data class PreviewIconPackObject(val code: String) : NewPackEvent
  data object OnSettingsUpdated : NewPackEvent
}
