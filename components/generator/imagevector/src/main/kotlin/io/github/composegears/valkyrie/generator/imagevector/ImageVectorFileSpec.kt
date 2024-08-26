package io.github.composegears.valkyrie.generator.imagevector

import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.LazyProperty
import io.github.composegears.valkyrie.generator.imagevector.spec.BackingPropertySpec
import io.github.composegears.valkyrie.generator.imagevector.spec.LazyPropertySpec
import io.github.composegears.valkyrie.ir.IrImageVector

internal data class ImageVectorSpecConfig(
    val iconName: String,
    val iconPack: String,
    val iconNestedPack: String,
    val iconPackage: String,
    val outputFormat: OutputFormat,
    val generatePreview: Boolean,
)

internal class ImageVectorFileSpec(private val config: ImageVectorSpecConfig) {

    fun createFileFor(vector: IrImageVector): ImageVectorSpecOutput {
        return when (config.outputFormat) {
            BackingProperty -> {
                BackingPropertySpec(config).createAsBackingProperty(vector)
            }
            LazyProperty -> {
                LazyPropertySpec(config).createAsLazyProperty(vector)
            }
        }
    }
}
