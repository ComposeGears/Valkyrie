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

class ExplicitModeTest {

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `generation with explicit mode`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                useExplicitMode = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.explicit.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.explicit.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
