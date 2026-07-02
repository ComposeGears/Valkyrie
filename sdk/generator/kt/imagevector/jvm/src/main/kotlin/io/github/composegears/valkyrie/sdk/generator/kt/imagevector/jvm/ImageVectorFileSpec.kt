package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm

import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorOutput
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.spec.BackingPropertySpec
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.spec.LazyPropertySpec
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

internal class ImageVectorFileSpec(private val config: ImageVectorGeneratorConfig) {

    fun createFileFor(vector: IrImageVector): ImageVectorOutput {
        return when (config.imageVector.outputFormat) {
            OutputFormat.BackingProperty -> BackingPropertySpec(config).createAsBackingProperty(vector)
            OutputFormat.LazyProperty -> LazyPropertySpec(config).createAsLazyProperty(vector)
        }
    }
}
