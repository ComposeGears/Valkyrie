package io.github.composegears.valkyrie.generator.kmp.imagevector.common

import io.github.composegears.valkyrie.generator.kmp.imagevector.FullQualifiedImports
import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.kmp.imagevector.OutputFormat

internal fun createConfig(
    packageName: String = "io.github.composegears.valkyrie.icons",
    iconPackPackage: String = packageName,
    packName: String = "",
    nestedPackName: String = "",
    outputFormat: OutputFormat,
    useComposeColors: Boolean = false,
    generatePreview: Boolean = false,
    useFlatPackage: Boolean = false,
    useExplicitMode: Boolean = false,
    addTrailingComma: Boolean = false,
    usePathDataString: Boolean = false,
    indentSize: Int = 4,
    fullQualifiedImports: FullQualifiedImports = FullQualifiedImports(),
): ImageVectorGeneratorConfig {
    return ImageVectorGeneratorConfig(
        packageName = packageName,
        iconPackPackage = iconPackPackage,
        packName = packName,
        nestedPackName = nestedPackName,
        outputFormat = outputFormat,
        useComposeColors = useComposeColors,
        generatePreview = generatePreview,
        useFlatPackage = useFlatPackage,
        useExplicitMode = useExplicitMode,
        addTrailingComma = addTrailingComma,
        usePathDataString = usePathDataString,
        indentSize = indentSize,
        fullQualifiedImports = fullQualifiedImports,
    )
}
