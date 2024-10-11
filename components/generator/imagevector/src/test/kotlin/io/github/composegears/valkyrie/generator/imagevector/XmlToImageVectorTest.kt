package io.github.composegears.valkyrie.generator.imagevector

import app.cash.burst.Burst
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import org.junit.Test

@Burst
class XmlToImageVectorTest {

    @Test
    fun `generation without icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `generation with nested icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                nestedPackName = "Colored",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.pack.nested.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.pack.nested.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `empty path xml`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.pack.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.pack.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `icon only with path`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_only_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/OnlyPath.kt",
            pathToLazyProperty = "kt/lazy/OnlyPath.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `icon with path and solid color`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_fill_color_stroke.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/FillColorStroke.kt",
            pathToLazyProperty = "kt/lazy/FillColorStroke.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `icon with all path params`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_all_path_params.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/AllPathParams.kt",
            pathToLazyProperty = "kt/lazy/AllPathParams.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `icon with several path`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_several_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/SeveralPath.kt",
            pathToLazyProperty = "kt/lazy/SeveralPath.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `icon with transparent fill color`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_transparent_fill_color.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/TransparentFillColor.kt",
            pathToLazyProperty = "kt/lazy/TransparentFillColor.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `icon with named arguments`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/icon_with_named_args.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/IconWithNamedArgs.kt",
            pathToLazyProperty = "kt/lazy/IconWithNamedArgs.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `icon with shorthand color`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/icon_with_shorthand_color.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/IconWithShorthandColor.kt",
            pathToLazyProperty = "kt/lazy/IconWithShorthandColor.kt",
        )
        assertThat(output).isEqualTo(expected)
    }
}
