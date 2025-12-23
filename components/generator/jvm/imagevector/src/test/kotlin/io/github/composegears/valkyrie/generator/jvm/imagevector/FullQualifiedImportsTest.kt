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
class FullQualifiedImportsTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `full qualified import, Brush`() {
        val icon = getResourcePath("imagevector/xml/ic_full_qualified.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "",
                outputFormat = outputFormat,
                useComposeColors = true,
                fullQualifiedImports = FullQualifiedImports(brush = true),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FullQualified.brush.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FullQualified.brush.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `full qualified import, Color`() {
        val icon = getResourcePath("imagevector/xml/ic_full_qualified.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "",
                outputFormat = outputFormat,
                useComposeColors = true,
                fullQualifiedImports = FullQualifiedImports(color = true),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FullQualified.color.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FullQualified.color.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `full qualified import, Offest`() {
        val icon = getResourcePath("imagevector/xml/ic_full_qualified.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "",
                outputFormat = outputFormat,
                useComposeColors = true,
                fullQualifiedImports = FullQualifiedImports(offset = true),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FullQualified.offset.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FullQualified.offset.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
