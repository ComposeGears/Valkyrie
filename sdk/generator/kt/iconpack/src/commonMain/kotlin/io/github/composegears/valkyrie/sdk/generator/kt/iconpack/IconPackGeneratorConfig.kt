package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree

public class IconPackGeneratorConfig(
    public val packageName: String,
    public val iconPackTree: IconPackTree,
    public val useExplicitMode: Boolean,
    public val indentSize: Int,
    public val license: String? = null,
)
