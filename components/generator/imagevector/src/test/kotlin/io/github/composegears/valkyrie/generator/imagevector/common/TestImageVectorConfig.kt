package io.github.composegears.valkyrie.generator.imagevector.common

import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat

internal fun createConfig(
    packName: String = "",
    nestedPackName: String = "",
    outputFormat: OutputFormat,
    generatePreview: Boolean = false,
    useFlatPackage: Boolean = false,
    useExplicitMode: Boolean = false,
    addTrailingComma: Boolean = false,
): ImageVectorGeneratorConfig {
    return ImageVectorGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        packName = packName,
        nestedPackName = nestedPackName,
        outputFormat = outputFormat,
        generatePreview = generatePreview,
        useFlatPackage = useFlatPackage,
        useExplicitMode = useExplicitMode,
        addTrailingComma = addTrailingComma,
    )
}
