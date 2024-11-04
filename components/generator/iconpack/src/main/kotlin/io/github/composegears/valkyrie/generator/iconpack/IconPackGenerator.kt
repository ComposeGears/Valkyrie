package io.github.composegears.valkyrie.generator.iconpack

data class IconPackGeneratorConfig(
    val packageName: String,
    val iconPackName: String,
    val subPacks: List<String>,
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
