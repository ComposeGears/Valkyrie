package io.github.composegears.valkyrie.generator.jvm.imagevector

import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat.LazyProperty
import io.github.composegears.valkyrie.generator.jvm.imagevector.spec.BackingPropertySpec
import io.github.composegears.valkyrie.generator.jvm.imagevector.spec.LazyPropertySpec
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

internal data class ImageVectorSpecConfig(
    val iconName: String,
    val iconPack: String,
    val iconPackPackage: String,
    val iconNestedPack: String,
    val iconPackage: String,
    val outputFormat: OutputFormat,
    val useComposeColors: Boolean,
    val generatePreview: Boolean,
    val previewAnnotationType: PreviewAnnotationType,
    val useFlatPackage: Boolean,
    val useExplicitMode: Boolean,
    val addTrailingComma: Boolean,
    val usePathDataString: Boolean,
    val indentSize: Int,
    val fullQualifiedImports: FullQualifiedImports,
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
