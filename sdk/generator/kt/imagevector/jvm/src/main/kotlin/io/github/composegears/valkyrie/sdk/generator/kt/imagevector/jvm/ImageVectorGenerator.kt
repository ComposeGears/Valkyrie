package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm

import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorOutput
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

public object ImageVectorGenerator {

    public fun convert(
        vector: IrImageVector,
        config: ImageVectorGeneratorConfig,
    ): ImageVectorOutput = ImageVectorFileSpec(config).createFileFor(vector)
}
