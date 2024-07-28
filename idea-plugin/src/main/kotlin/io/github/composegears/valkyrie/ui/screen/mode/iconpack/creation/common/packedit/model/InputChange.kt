package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model

sealed interface InputChange {
    data class PackageName(val text: String) : InputChange
    data class IconPackName(val text: String) : InputChange
    data class NestedPackName(val id: String, val text: String) : InputChange
}
