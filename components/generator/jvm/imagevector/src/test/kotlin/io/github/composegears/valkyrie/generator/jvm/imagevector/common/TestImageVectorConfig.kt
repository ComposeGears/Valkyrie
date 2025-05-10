package io.github.composegears.valkyrie.generator.jvm.imagevector.common

import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType

internal fun createConfig(
    packageName: String = "io.github.composegears.valkyrie.icons",
    iconPackPackage: String = packageName,
    packName: String = "",
    nestedPackName: String = "",
    outputFormat: OutputFormat,
    useComposeColors: Boolean = false,
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
        useComposeColors = useComposeColors,
        generatePreview = generatePreview,
        previewAnnotationType = previewAnnotationType,
        useFlatPackage = useFlatPackage,
        useExplicitMode = useExplicitMode,
        addTrailingComma = addTrailingComma,
        indentSize = indentSize,
    )
}
