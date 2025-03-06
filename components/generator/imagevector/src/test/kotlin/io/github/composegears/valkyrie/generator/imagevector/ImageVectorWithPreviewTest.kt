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

class ImageVectorWithPreviewTest {

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `androidx preview generation without icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                generatePreview = true,
                previewAnnotationType = PreviewAnnotationType.AndroidX,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.preview.androidx.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.preview.androidx.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `jetbrains preview generation without icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                outputFormat = outputFormat,
                generatePreview = true,
                previewAnnotationType = PreviewAnnotationType.Jetbrains,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.preview.jetbrains.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.preview.jetbrains.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `androidx preview generation with icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                generatePreview = true,
                previewAnnotationType = PreviewAnnotationType.AndroidX,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.preview.androidx.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.preview.androidx.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `jetbrains preview generation with icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                generatePreview = true,
                previewAnnotationType = PreviewAnnotationType.Jetbrains,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.preview.jetbrains.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.preview.jetbrains.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `androidx preview generation with nested pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                nestedPackName = "Filled",
                outputFormat = outputFormat,
                generatePreview = true,
                previewAnnotationType = PreviewAnnotationType.AndroidX,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.nested.preview.androidx.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.nested.preview.androidx.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(value = OutputFormat::class)
    fun `jetbrains preview generation with nested pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            iconName = parserOutput.iconName,
            config = createConfig(
                packName = "ValkyrieIcons",
                nestedPackName = "Filled",
                outputFormat = outputFormat,
                generatePreview = true,
                previewAnnotationType = PreviewAnnotationType.Jetbrains,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.nested.preview.jetbrains.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.nested.preview.jetbrains.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
