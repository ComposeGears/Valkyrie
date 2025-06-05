package io.github.composegears.valkyrie.generator.iconpack

import io.github.composegears.valkyrie.generator.core.IconPack

data class IconPackGeneratorConfig(
    val packageName: String,
    val iconPack: IconPack,
    val useExplicitMode: Boolean,
    val indentSize: Int,
)
