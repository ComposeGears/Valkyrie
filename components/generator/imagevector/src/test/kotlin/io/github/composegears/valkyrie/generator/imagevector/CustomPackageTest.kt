package io.github.composegears.valkyrie.generator.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class CustomPackageTest {

    private fun createConfig(
        packageName: String = "io.github.composegears.valkyrie.icons",
        packName: String = "",
        nestedPackName: String = "",
        outputFormat: OutputFormat,
        generatePreview: Boolean = false,
        useFlatPackage: Boolean = true,
        useExplicitMode: Boolean = false,
    ): ImageVectorGeneratorConfig {
        return ImageVectorGeneratorConfig(
            packageName = packageName,
            packName = packName,
            nestedPackName = nestedPackName,
            outputFormat = outputFormat,
            generatePreview = generatePreview,
            useFlatPackage = useFlatPackage,
            useExplicitMode = useExplicitMode,
        )
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `flat package without icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/FlatPackage.kt",
            pathToLazyProperty = "kt/lazy/FlatPackage.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `flat package with icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/FlatPackage.pack.kt",
            pathToLazyProperty = "kt/lazy/FlatPackage.pack.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `flat package with nested icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                nestedPackName = "Filled",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/FlatPackage.pack.nested.kt",
            pathToLazyProperty = "kt/lazy/FlatPackage.pack.nested.kt",
        )
        assertThat(output).isEqualTo(expected)
    }
}
