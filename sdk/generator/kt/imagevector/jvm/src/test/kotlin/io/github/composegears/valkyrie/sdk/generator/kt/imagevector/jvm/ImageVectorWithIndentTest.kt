package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.model.IconType.XML
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.CodeStyleConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.testfixtures.DEFAULT_PACKAGE
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.testfixtures.toResourceText
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
            config = ImageVectorGeneratorConfig.simple(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                codeStyle = CodeStyleConfig(indentSize = 1),
                imageVector = ImageVectorConfig(outputFormat = outputFormat),
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
            config = ImageVectorGeneratorConfig.simple(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                codeStyle = CodeStyleConfig(indentSize = 2),
                imageVector = ImageVectorConfig(outputFormat = outputFormat),
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
            config = ImageVectorGeneratorConfig.simple(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                codeStyle = CodeStyleConfig(indentSize = 3),
                imageVector = ImageVectorConfig(outputFormat = outputFormat),
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
            config = ImageVectorGeneratorConfig.simple(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                codeStyle = CodeStyleConfig(indentSize = 6),
                imageVector = ImageVectorConfig(outputFormat = outputFormat),
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
