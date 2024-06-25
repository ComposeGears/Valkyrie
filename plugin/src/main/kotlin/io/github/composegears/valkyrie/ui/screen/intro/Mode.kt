package io.github.composegears.valkyrie.ui.screen.intro

enum class Mode {
    Simple,
    IconPack,
    Unspecified;

    companion object {
        fun Mode.isUnspecified() = this == Unspecified
    }
}