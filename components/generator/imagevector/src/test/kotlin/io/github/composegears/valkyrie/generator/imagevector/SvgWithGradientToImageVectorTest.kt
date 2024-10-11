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
class SvgWithGradientToImageVectorTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `svg linear gradient parsing`() {
        val icon = getResourcePath("svg/ic_linear_gradient.svg")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/LinearGradient.kt",
            pathToLazyProperty = "kt/lazy/LinearGradient.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `svg radial gradient parsing`() {
        val icon = getResourcePath("svg/ic_radial_gradient.svg")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/RadialGradient.kt",
            pathToLazyProperty = "kt/lazy/RadialGradient.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `svg linear gradient with stroke parsing`() {
        val icon = getResourcePath("svg/ic_linear_gradient_with_stroke.svg")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/LinearGradientWithStroke.kt",
            pathToLazyProperty = "kt/lazy/LinearGradientWithStroke.kt",
        )
        assertThat(output).isEqualTo(expected)
    }
}
