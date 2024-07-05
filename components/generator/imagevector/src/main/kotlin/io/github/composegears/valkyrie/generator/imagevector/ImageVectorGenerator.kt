package io.github.composegears.valkyrie.generator.imagevector

import androidx.compose.material.icons.generator.vector.Vector

data class ImageVectorGeneratorConfig(
    val packageName: String,
    val packName: String,
    val nestedPackName: String,
    val generatePreview: Boolean
)

data class ImageVectorSpecOutput(
    val content: String,
    val name: String
) {
    companion object {
        val empty = ImageVectorSpecOutput(
            content = "",
            name = ""
        )
    }
}

object ImageVectorGenerator {

    fun convert(
        vector: Vector,
        kotlinName: String,
        config: ImageVectorGeneratorConfig
    ): ImageVectorSpecOutput = ImageVectorFileSpec(
        config = ImageVectorSpecConfig(
            iconPackage = config.packageName,
            iconPack = config.packName,
            iconName = kotlinName,
            iconNestedPack = config.nestedPackName,
            generatePreview = config.generatePreview
        )
    ).createFileFor(vector)
}