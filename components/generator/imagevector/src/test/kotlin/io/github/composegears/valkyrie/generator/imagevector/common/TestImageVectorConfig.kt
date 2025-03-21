package io.github.composegears.valkyrie.generator.imagevector.common

import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.imagevector.PreviewAnnotationType

internal fun createConfig(
    packageName: String = "io.github.composegears.valkyrie.icons",
    iconPackPackage: String = packageName,
    packName: String = "",
    nestedPackName: String = "",
    outputFormat: OutputFormat,
    generatePreview: Boolean = false,
    previewAnnotationType: PreviewAnnotationType = PreviewAnnotationType.AndroidX,
    useFlatPackage: Boolean = false,
    useExplicitMode: Boolean = false,
    addTrailingComma: Boolean = false,
    indentSize: Int = 4,
): ImageVectorGeneratorConfig {
    return ImageVectorGeneratorConfig(
        packageName = packageName,
        iconPackPackage = iconPackPackage,
        packName = packName,
        nestedPackName = nestedPackName,
        outputFormat = outputFormat,
        generatePreview = generatePreview,
        previewAnnotationType = previewAnnotationType,
        useFlatPackage = useFlatPackage,
        useExplicitMode = useExplicitMode,
        addTrailingComma = addTrailingComma,
        indentSize = indentSize,
    )
}
