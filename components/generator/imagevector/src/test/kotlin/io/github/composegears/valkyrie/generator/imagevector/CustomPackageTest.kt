package io.github.composegears.valkyrie.generator.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.XML
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class CustomPackageTest {

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `flat package without icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                useFlatPackage = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FlatPackage.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FlatPackage.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `flat package with icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                useFlatPackage = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FlatPackage.pack.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FlatPackage.pack.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `flat package with nested icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                nestedPackName = "Filled",
                outputFormat = outputFormat,
                useFlatPackage = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FlatPackage.pack.nested.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FlatPackage.pack.nested.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `different icon pack package`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                iconPackPackage = "androidx.compose.material.icons",
                useFlatPackage = true,
                packName = "Icons",
                nestedPackName = "Filled",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.package.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.package.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
