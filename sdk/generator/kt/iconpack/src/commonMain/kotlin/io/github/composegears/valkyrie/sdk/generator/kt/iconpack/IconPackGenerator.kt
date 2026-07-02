package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

public expect object IconPackGenerator {
    public fun create(config: IconPackGeneratorConfig): IconPackSpecOutput
}
