package io.github.composegears.valkyrie.generator.imagevector.common

import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat

internal fun createConfig(
    packageName: String = "io.github.composegears.valkyrie.icons",
    iconPackPackage: String = packageName,
    packName: String = "",
    nestedPackName: String = "",
    outputFormat: OutputFormat,
    generatePreview: Boolean = false,
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
        useFlatPackage = useFlatPackage,
        useExplicitMode = useExplicitMode,
        addTrailingComma = addTrailingComma,
        indentSize = indentSize,
    )
}
