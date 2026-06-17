package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.model.IconType.SVG
import io.github.composegears.valkyrie.parser.unified.model.IconType.XML
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
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
class PathDataStringTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `icon with path data string format`() {
        val icon = getResourcePath("imagevector/xml/ic_only_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.iconPack(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons"),
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    usePathDataString = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/OnlyPathWithPathData.kt",
            pathToLazyProperty = "imagevector/kt/lazy/OnlyPathWithPathData.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `clip path uses path data string format when enabled`() {
        val icon = getResourcePath("imagevector/svg/ic_clip_path_gradient.svg").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.iconPack(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons"),
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    usePathDataString = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/ClipPathGradientWithPathData.kt",
            pathToLazyProperty = "imagevector/kt/lazy/ClipPathGradientWithPathData.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(SVG)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `clip path uses builder format when string mode is disabled`() {
        val icon = getResourcePath("imagevector/svg/ic_clip_path_gradient.svg").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.simple(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                imageVector = ImageVectorConfig(outputFormat = outputFormat),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/ClipPathGradient.kt",
            pathToLazyProperty = "imagevector/kt/lazy/ClipPathGradient.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(SVG)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `simple clip path with smooth curves uses path data string format`() {
        val icon = getResourcePath("imagevector/xml/ic_clip_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.iconPack(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons"),
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    useComposeColors = false,
                    usePathDataString = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/ClipPathWithPathData.kt",
            pathToLazyProperty = "imagevector/kt/lazy/ClipPathWithPathData.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
