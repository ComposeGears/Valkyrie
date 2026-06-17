package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree

class IconPackGeneratorConfig(
    val packageName: String,
    val iconPackTree: IconPackTree,
    val useExplicitMode: Boolean,
    val indentSize: Int,
    val license: String? = null,
)
