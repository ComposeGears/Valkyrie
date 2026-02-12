package io.github.composegears.valkyrie.generator.jvm.imagevector

import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

data class ImageVectorGeneratorConfig(
    val packageName: String,
    val iconPackPackage: String,
    val packName: String,
    val nestedPackName: String,
    val outputFormat: OutputFormat,
    val useComposeColors: Boolean,
    val generatePreview: Boolean,
    val useFlatPackage: Boolean,
    val useExplicitMode: Boolean,
    val addTrailingComma: Boolean,
    val usePathDataString: Boolean,
    val indentSize: Int,
    val fullQualifiedImports: FullQualifiedImports = FullQualifiedImports(),
)

data class FullQualifiedImports(
    val brush: Boolean = false,
    val color: Boolean = false,
    val offset: Boolean = false,
) {
    companion object {
        val reservedComposeQualifiers = listOf("Brush", "Color", "Offset")
    }
}

enum class OutputFormat(val key: String) {
    BackingProperty(key = "backing_property"),
    LazyProperty(key = "lazy_property"),
    ;

    companion object {
        fun from(key: String?) = entries.find { it.key == key } ?: BackingProperty
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
            useFlatPackage = config.useFlatPackage,
            useExplicitMode = config.useExplicitMode,
            addTrailingComma = config.addTrailingComma,
            usePathDataString = config.usePathDataString,
            indentSize = config.indentSize,
            fullQualifiedImports = config.fullQualifiedImports,
        ),
    ).createFileFor(vector)
}
