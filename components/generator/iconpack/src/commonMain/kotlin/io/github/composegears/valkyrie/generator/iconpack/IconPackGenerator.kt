package io.github.composegears.valkyrie.generator.iconpack

expect object IconPackGenerator {
    fun create(config: IconPackGeneratorConfig): IconPackSpecOutput
}
