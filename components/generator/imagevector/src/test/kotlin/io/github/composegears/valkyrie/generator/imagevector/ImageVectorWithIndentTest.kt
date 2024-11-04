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

class ImageVectorWithIndentTest {

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `generation without icon pack with indent 1`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                indentSize = 1,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.indent1.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.indent1.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `generation without icon pack with indent 2`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                indentSize = 2,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.indent2.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.indent2.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `generation without icon pack with indent 3`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                indentSize = 3,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.indent3.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.indent3.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `generation without icon pack with indent 6`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                indentSize = 6,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.indent6.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.indent6.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
