package io.github.composegears.valkyrie.generator.jvm.imagevector

import io.github.composegears.valkyrie.ir.IrImageVector

data class ImageVectorGeneratorConfig(
    val packageName: String,
    val iconPackPackage: String,
    val packName: String,
    val nestedPackName: String,
    val outputFormat: OutputFormat,
    val useComposeColors: Boolean,
    val generatePreview: Boolean,
    val previewAnnotationType: PreviewAnnotationType,
    val useFlatPackage: Boolean,
    val useExplicitMode: Boolean,
    val addTrailingComma: Boolean,
    val indentSize: Int,
)

enum class PreviewAnnotationType(val key: String) {
    AndroidX("androidx"),
    Jetbrains("jetbrains"),
    ;

    companion object {
        fun from(key: String?) = PreviewAnnotationType.entries.find { it.key == key } ?: AndroidX
    }
}

enum class OutputFormat(val key: String) {
    BackingProperty(key = "backing_property"),
    LazyProperty(key = "lazy_property"),
    ;

    companion object {
        fun from(key: String?) = OutputFormat.entries.find { it.key == key } ?: BackingProperty
    }
}

data class ImageVectorSpecOutput(
    val content: String,
    val name: String,
)

object ImageVectorGenerator {

    fun convert(
        vector: IrImageVector,
        iconName: String,
        config: ImageVectorGeneratorConfig,
    ): ImageVectorSpecOutput = ImageVectorFileSpec(
        config = ImageVectorSpecConfig(
            iconPackage = config.packageName,
            iconPack = config.packName,
            iconPackPackage = config.iconPackPackage,
            iconName = iconName,
            iconNestedPack = config.nestedPackName,
            outputFormat = config.outputFormat,
            useComposeColors = config.useComposeColors,
            generatePreview = config.generatePreview,
            previewAnnotationType = config.previewAnnotationType,
            useFlatPackage = config.useFlatPackage,
            useExplicitMode = config.useExplicitMode,
            addTrailingComma = config.addTrailingComma,
            indentSize = config.indentSize,
        ),
    ).createFileFor(vector)
}
