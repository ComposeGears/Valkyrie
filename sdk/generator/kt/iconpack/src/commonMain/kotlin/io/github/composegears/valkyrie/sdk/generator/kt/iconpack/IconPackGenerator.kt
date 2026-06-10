package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

expect object IconPackGenerator {
    fun create(config: IconPackGeneratorConfig): IconPackSpecOutput
}
