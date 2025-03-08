package io.github.composegears.valkyrie.generator.iconpack

import io.github.composegears.valkyrie.generator.model.IconPack

data class IconPackGeneratorConfig(
    val packageName: String,
    val iconPack: IconPack,
    val useExplicitMode: Boolean,
    val indentSize: Int,
)

data class IconPackSpecOutput(
    val content: String,
    val name: String,
)

object IconPackGenerator {

    fun create(config: IconPackGeneratorConfig) = IconPackFileSpec(config).createSpec()
}
