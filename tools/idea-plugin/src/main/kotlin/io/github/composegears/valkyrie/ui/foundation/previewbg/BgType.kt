package io.github.composegears.valkyrie.ui.foundation.previewbg

enum class BgType {
    Black,
    White,
    PixelGrid,
    ;

    fun next(): BgType = ordinal.let { entries[(it + 1) % entries.size] }
}
