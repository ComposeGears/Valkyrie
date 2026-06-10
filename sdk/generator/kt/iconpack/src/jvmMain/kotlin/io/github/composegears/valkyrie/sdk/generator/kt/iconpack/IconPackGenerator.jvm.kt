package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.internal.IconPackFileSpec

actual object IconPackGenerator {
    actual fun create(config: IconPackGeneratorConfig): IconPackSpecOutput {
        return IconPackFileSpec(config).createSpec()
    }
}
