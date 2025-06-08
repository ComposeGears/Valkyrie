package io.github.composegears.valkyrie.generator.jvm.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.model.IconType.XML
import io.github.composegears.valkyrie.resource.loader.ResourceLoader.getResourcePath
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedClass
import org.junit.jupiter.params.provider.EnumSource

@ParameterizedClass
@EnumSource(value = OutputFormat::class)
class ImageVectorWithPreviewTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `androidx preview generation without icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
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

    @Test
    fun `jetbrains preview generation without icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
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

    @Test
    fun `androidx preview generation with icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
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

    @Test
    fun `jetbrains preview generation with icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
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

    @Test
    fun `androidx preview generation with nested pack`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
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

    @Test
    fun `jetbrains preview generation with nested pack`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
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
