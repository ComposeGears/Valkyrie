package io.github.composegears.valkyrie.generator.jvm.imagevector

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.XML
import kotlin.io.path.Path
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class XmlToImageVectorTest {

    @Test
    fun `broken icon path should throw exception`() {
        val brokenIconPath = Path("")

        assertFailure {
            SvgXmlParser.toIrImageVector(brokenIconPath)
        }.isInstanceOf(IllegalStateException::class)
            .hasMessage(" must be an SVG or XML file.")
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `generation without icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(outputFormat = outputFormat),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `generation with nested icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                nestedPackName = "Colored",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.nested.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.nested.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `empty path xml`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon only with path`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_only_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/OnlyPath.kt",
            pathToLazyProperty = "imagevector/kt/lazy/OnlyPath.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with path and solid color`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_fill_color_stroke.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FillColorStroke.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FillColorStroke.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with all path params`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_all_path_params.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/AllPathParams.kt",
            pathToLazyProperty = "imagevector/kt/lazy/AllPathParams.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with all group params`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_all_group_params.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/AllGroupParams.kt",
            pathToLazyProperty = "imagevector/kt/lazy/AllGroupParams.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with several path`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_several_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/SeveralPath.kt",
            pathToLazyProperty = "imagevector/kt/lazy/SeveralPath.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with compose colors enabled`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_compose_color.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                useComposeColors = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/ComposeColor.kt",
            pathToLazyProperty = "imagevector/kt/lazy/ComposeColor.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with compose colors and linear gradient`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_compose_color_linear_gradient.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "",
                outputFormat = outputFormat,
                useComposeColors = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/ComposeColor.linear.gradient.kt",
            pathToLazyProperty = "imagevector/kt/lazy/ComposeColor.linear.gradient.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with compose colors and radial gradient`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_compose_color_radial_gradient.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "",
                outputFormat = outputFormat,
                useComposeColors = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/ComposeColor.radial.gradient.kt",
            pathToLazyProperty = "imagevector/kt/lazy/ComposeColor.radial.gradient.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with transparent fill color`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_transparent_fill_color.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/TransparentFillColor.kt",
            pathToLazyProperty = "imagevector/kt/lazy/TransparentFillColor.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with named arguments`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/icon_with_named_args.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/IconWithNamedArgs.kt",
            pathToLazyProperty = "imagevector/kt/lazy/IconWithNamedArgs.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `icon with shorthand color`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/icon_with_shorthand_color.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/IconWithShorthandColor.kt",
            pathToLazyProperty = "imagevector/kt/lazy/IconWithShorthandColor.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
