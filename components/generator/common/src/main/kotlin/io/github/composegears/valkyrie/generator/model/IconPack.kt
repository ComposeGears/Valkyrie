package io.github.composegears.valkyrie.generator.model

data class IconPack(
    val name: String,
    val nested: List<IconPack> = emptyList(),
)
