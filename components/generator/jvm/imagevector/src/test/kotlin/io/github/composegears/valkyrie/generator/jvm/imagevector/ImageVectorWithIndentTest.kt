package io.github.composegears.valkyrie.generator.jvm.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.model.IconType.XML
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourcePath
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedClass
import org.junit.jupiter.params.provider.EnumSource

@ParameterizedClass
@EnumSource(value = OutputFormat::class)
class ImageVectorWithIndentTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `generation without icon pack with indent 1`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                indentSize = 1,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.indent1.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.indent1.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `generation without icon pack with indent 2`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                indentSize = 2,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.indent2.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.indent2.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `generation without icon pack with indent 3`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                indentSize = 3,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.indent3.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.indent3.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `generation without icon pack with indent 6`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                indentSize = 6,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.indent6.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.indent6.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
