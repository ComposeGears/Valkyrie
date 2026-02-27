package io.github.composegears.valkyrie.generator.kmp.imagevector.render

import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorOutput
import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorRenderConfig
import io.github.composegears.valkyrie.generator.kmp.imagevector.util.ImageVectorRenderer
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

internal class BackingPropertyRenderer(private val config: ImageVectorRenderConfig) {
    fun render(irVector: IrImageVector): ImageVectorOutput = ImageVectorOutput(
        content = ImageVectorRenderer.renderBacking(config = config, vector = irVector),
        name = config.iconName,
    )
}
