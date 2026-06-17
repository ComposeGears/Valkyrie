package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.render

import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorOutput
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.util.ImageVectorRenderer
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

internal class BackingPropertyRenderer(private val config: ImageVectorGeneratorConfig) {

    fun render(irVector: IrImageVector): ImageVectorOutput = ImageVectorOutput(
        content = ImageVectorRenderer.renderBacking(
            config = config,
            vector = irVector,
        ),
        name = config.iconName,
    )
}
