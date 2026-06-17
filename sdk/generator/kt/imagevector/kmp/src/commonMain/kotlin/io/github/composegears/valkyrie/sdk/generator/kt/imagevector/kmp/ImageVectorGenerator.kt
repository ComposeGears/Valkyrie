package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp

import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorOutput
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.render.BackingPropertyRenderer
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.render.LazyPropertyRenderer
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

object ImageVectorGenerator {

    fun convert(
        vector: IrImageVector,
        config: ImageVectorGeneratorConfig,
    ): ImageVectorOutput = when (config.imageVector.outputFormat) {
        OutputFormat.BackingProperty -> BackingPropertyRenderer(config).render(vector)
        OutputFormat.LazyProperty -> LazyPropertyRenderer(config).render(vector)
    }
}
