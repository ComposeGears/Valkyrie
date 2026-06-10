package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

import io.github.composegears.valkyrie.generator.core.IconPack

class IconPackGeneratorConfig(
    val packageName: String,
    val iconPack: IconPack,
    val useExplicitMode: Boolean,
    val indentSize: Int,
    val license: String? = null,
)
