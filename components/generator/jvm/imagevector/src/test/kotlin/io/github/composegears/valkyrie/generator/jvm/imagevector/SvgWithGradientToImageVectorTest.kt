package io.github.composegears.valkyrie.generator.jvm.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.model.IconType.SVG
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedClass
import org.junit.jupiter.params.provider.EnumSource

@ParameterizedClass
@EnumSource(value = OutputFormat::class)
class SvgWithGradientToImageVectorTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `svg linear gradient parsing`() {
        val icon = getResourcePath("imagevector/svg/ic_linear_gradient.svg").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/LinearGradient.kt",
            pathToLazyProperty = "imagevector/kt/lazy/LinearGradient.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(SVG)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `svg radial gradient parsing`() {
        val icon = getResourcePath("imagevector/svg/ic_radial_gradient.svg").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/RadialGradient.kt",
            pathToLazyProperty = "imagevector/kt/lazy/RadialGradient.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(SVG)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `svg linear gradient with stroke parsing`() {
        val icon = getResourcePath("imagevector/svg/ic_linear_gradient_with_stroke.svg").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/LinearGradientWithStroke.kt",
            pathToLazyProperty = "imagevector/kt/lazy/LinearGradientWithStroke.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(SVG)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `svg with complex gradient`() {
        val icon = getResourcePath("imagevector/svg/ic_clip_path_gradient.svg").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/ClipPathGradient.kt",
            pathToLazyProperty = "imagevector/kt/lazy/ClipPathGradient.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(SVG)
        assertThat(output).isEqualTo(expected)
    }
}
