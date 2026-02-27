package io.github.composegears.valkyrie.generator.kmp.imagevector

import io.github.composegears.valkyrie.generator.kmp.imagevector.render.BackingPropertyRenderer
import io.github.composegears.valkyrie.generator.kmp.imagevector.render.LazyPropertyRenderer
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

internal data class ImageVectorRenderConfig(
    val iconName: String,
    val iconPack: String,
    val iconPackPackage: String,
    val iconNestedPack: String,
    val iconPackage: String,
    val outputFormat: OutputFormat,
    val useComposeColors: Boolean,
    val generatePreview: Boolean,
    val useFlatPackage: Boolean,
    val useExplicitMode: Boolean,
    val addTrailingComma: Boolean,
    val usePathDataString: Boolean,
    val indentSize: Int,
    val fullQualifiedImports: FullQualifiedImports,
)

internal class ImageVectorFileGenerator(private val config: ImageVectorRenderConfig) {
    fun create(vector: IrImageVector): ImageVectorOutput = when (config.outputFormat) {
        OutputFormat.BackingProperty -> BackingPropertyRenderer(config).render(vector)
        OutputFormat.LazyProperty -> LazyPropertyRenderer(config).render(vector)
    }
}
