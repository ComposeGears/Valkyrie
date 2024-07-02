package io.github.composegears.valkyrie.processing.generator.imagevector

import com.squareup.kotlinpoet.ClassName
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
            iconPack = when {
                config.packName.isEmpty() -> null
                else -> {
                    if (config.nestedPackName.isEmpty()) {
                        ClassName(
                            config.packageName,
                            config.packName
                        )
                    } else {
                        ClassName(
                            config.packageName,
                            config.packName
                        ).nestedClass(config.nestedPackName)
                    }
                }
            },
            iconName = parserOutput.kotlinName,
            iconNestedPack = config.nestedPackName,
            generatePreview = config.generatePreview
        )
    ).createFileFor(parserOutput.vector)
}