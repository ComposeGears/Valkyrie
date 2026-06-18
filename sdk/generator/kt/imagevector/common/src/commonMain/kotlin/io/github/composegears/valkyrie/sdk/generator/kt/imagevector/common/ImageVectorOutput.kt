package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

/**
 * Result produced by the ImageVector generator.
 *
 * @property content Generated Kotlin source code as a plain string.
 * @property name Simple name of the generated icon (matches [ImageVectorGeneratorConfig.iconName]).
 */
public class ImageVectorOutput(
    public val content: String,
    public val name: String,
)
