package io.github.composegears.valkyrie.generator.imagevector

import androidx.compose.material.icons.generator.vector.Vector
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.LazyDelegateProperty
import io.github.composegears.valkyrie.generator.imagevector.spec.BackingPropertySpec
import io.github.composegears.valkyrie.generator.imagevector.spec.LazyDelegatePropertySpec

internal data class ImageVectorSpecConfig(
    val iconName: String,
    val iconPack: String,
    val iconNestedPack: String,
    val iconPackage: String,
    val outputFormat: OutputFormat,
    val generatePreview: Boolean,
)

internal class ImageVectorFileSpec(private val config: ImageVectorSpecConfig) {

    fun createFileFor(vector: Vector): ImageVectorSpecOutput {
        return when (config.outputFormat) {
            BackingProperty -> {
                BackingPropertySpec(config).createAsBackingProperty(vector)
            }
            LazyDelegateProperty -> {
                LazyDelegatePropertySpec(config).createAsLazyDelegateProperty(vector)
            }
        }
    }
}
