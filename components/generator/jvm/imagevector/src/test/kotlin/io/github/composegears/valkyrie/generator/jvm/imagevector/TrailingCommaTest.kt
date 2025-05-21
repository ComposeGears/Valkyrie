package io.github.composegears.valkyrie.generator.jvm.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.model.IconType.XML
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class TrailingCommaTest {

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with path and solid color`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_fill_color_stroke.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                addTrailingComma = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FillColorStroke.trailing.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FillColorStroke.trailing.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with all group params trailing`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_all_group_params.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                addTrailingComma = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/AllGroupParams.trailing.kt",
            pathToLazyProperty = "imagevector/kt/lazy/AllGroupParams.trailing.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
