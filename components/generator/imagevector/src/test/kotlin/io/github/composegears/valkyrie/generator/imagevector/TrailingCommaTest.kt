package io.github.composegears.valkyrie.generator.imagevector

import app.cash.burst.Burst
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import org.junit.jupiter.api.Test

@Burst
class TrailingCommaTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `icon with path and solid color`() {
        val icon = getResourcePath("xml/ic_fill_color_stroke.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                addTrailingComma = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/FillColorStroke.trailing.kt",
            pathToLazyProperty = "kt/lazy/FillColorStroke.trailing.kt",
        )
        assertThat(output).isEqualTo(expected)
    }
}
