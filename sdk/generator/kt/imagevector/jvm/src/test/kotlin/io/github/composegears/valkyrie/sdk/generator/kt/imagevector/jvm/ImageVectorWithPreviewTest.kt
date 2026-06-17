package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.model.IconType.XML
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.testfixtures.DEFAULT_PACKAGE
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.testfixtures.toResourceText
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourcePath
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
            config = ImageVectorGeneratorConfig.simple(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    generatePreview = true,
                ),
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
    fun `androidx preview generation with icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.iconPack(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons"),
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    generatePreview = true,
                ),
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
    fun `androidx preview generation with nested pack`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.iconPack(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons") {
                    child("Filled")
                },
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    generatePreview = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.nested.preview.androidx.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.nested.preview.androidx.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
