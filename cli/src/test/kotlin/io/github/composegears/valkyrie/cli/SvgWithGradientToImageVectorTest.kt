package io.github.composegears.valkyrie.cli

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.parser.IconParser
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class SvgWithGradientToImageVectorTest {

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `svg linear gradient parsing`(outputFormat: OutputFormat) {
        val icon = getResourcePath("svg/ic_linear_gradient.svg")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = ImageVectorGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                packName = "",
                nestedPackName = "",
                outputFormat = outputFormat,
                generatePreview = false,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/LinearGradient.kt",
            pathToLazyProperty = "kt/lazy/LinearGradient.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `svg radial gradient parsing`(outputFormat: OutputFormat) {
        val icon = getResourcePath("svg/ic_radial_gradient.svg")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = ImageVectorGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                packName = "",
                nestedPackName = "",
                outputFormat = outputFormat,
                generatePreview = false,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/RadialGradient.kt",
            pathToLazyProperty = "kt/lazy/RadialGradient.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `svg linear gradient with stroke parsing`(outputFormat: OutputFormat) {
        val icon = getResourcePath("svg/ic_linear_gradient_with_stroke.svg")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = ImageVectorGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                packName = "",
                nestedPackName = "",
                outputFormat = outputFormat,
                generatePreview = false,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/LinearGradientWithStroke.kt",
            pathToLazyProperty = "kt/lazy/LinearGradientWithStroke.kt",
        )
        assertThat(output).isEqualTo(expected)
    }
}
