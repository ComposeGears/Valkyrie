package io.github.composegears.valkyrie.generator.iconpack

actual object IconPackGenerator {
    actual fun create(config: IconPackGeneratorConfig): IconPackSpecOutput {
        return IconPackFileSpec(config).createSpec()
    }
}
