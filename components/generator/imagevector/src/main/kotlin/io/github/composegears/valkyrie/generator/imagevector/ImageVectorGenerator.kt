package io.github.composegears.valkyrie.generator.imagevector

import androidx.compose.material.icons.generator.vector.Vector
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.BackingProperty

data class ImageVectorGeneratorConfig(
    val packageName: String,
    val packName: String,
    val nestedPackName: String,
    val outputFormat: OutputFormat = BackingProperty,
    val generatePreview: Boolean,
)

enum class OutputFormat(val key: String) {
    BackingProperty(key = "backing_property"),
    LazyDelegateProperty(key = "lazy_delegate_property"),
    ;

    companion object {
        fun from(key: String?): OutputFormat {
            return OutputFormat.valueOf(key ?: BackingProperty.key)
        }
    }
}

data class ImageVectorSpecOutput(
    val content: String,
    val name: String,
) {
    companion object {
        val empty = ImageVectorSpecOutput(
            content = "",
            name = "",
        )
    }
}

object ImageVectorGenerator {

    fun convert(
        vector: Vector,
        kotlinName: String,
        config: ImageVectorGeneratorConfig,
    ): ImageVectorSpecOutput = ImageVectorFileSpec(
        config = ImageVectorSpecConfig(
            iconPackage = config.packageName,
            iconPack = config.packName,
            iconName = kotlinName,
            iconNestedPack = config.nestedPackName,
            outputFormat = config.outputFormat,
            generatePreview = config.generatePreview,
        ),
    ).createFileFor(vector)
}
