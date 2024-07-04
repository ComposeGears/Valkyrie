package io.github.composegears.valkyrie.processing.generator.imagevector

import io.github.composegears.valkyrie.processing.parser.IconParserOutput

data class ImageVectorGeneratorConfig(
    val packageName: String,
    val packName: String,
    val nestedPackName: String,
    val generatePreview: Boolean
)

object ImageVectorGenerator {

    fun convert(
        parserOutput: IconParserOutput,
        config: ImageVectorGeneratorConfig
    ): ImageVectorSpecOutput = ImageVectorFileSpec(
        config = ImageVectorSpecConfig(
            iconPackage = config.packageName,
            iconPack = config.packName,
            iconName = parserOutput.kotlinName,
            iconNestedPack = config.nestedPackName,
            generatePreview = config.generatePreview
        )
    ).createFileFor(parserOutput.vector)
}