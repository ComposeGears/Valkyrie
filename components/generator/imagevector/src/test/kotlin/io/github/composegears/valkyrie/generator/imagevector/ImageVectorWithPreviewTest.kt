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

class ImageVectorWithPreviewTest {

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `preview generation without icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                generatePreview = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.preview.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.preview.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `preview generation with icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                generatePreview = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.pack.preview.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.pack.preview.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `preview generation with nested pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                nestedPackName = "Filled",
                outputFormat = outputFormat,
                generatePreview = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.pack.nested.preview.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.pack.nested.preview.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
