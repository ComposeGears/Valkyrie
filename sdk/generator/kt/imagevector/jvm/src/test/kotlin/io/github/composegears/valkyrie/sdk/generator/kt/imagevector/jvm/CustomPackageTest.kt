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
class CustomPackageTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `flat package without icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_flat_package.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.simple(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    useFlatPackage = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FlatPackage.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FlatPackage.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `flat package with icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_flat_package.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.iconPack(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons"),
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    useFlatPackage = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FlatPackage.pack.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FlatPackage.pack.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `flat package with nested icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_flat_package.xml").toIOPath()
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
                    useFlatPackage = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FlatPackage.pack.nested.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FlatPackage.pack.nested.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `flat package with deep nested icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_flat_package.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.iconPack(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons") {
                    child("Material") {
                        child("Rounded")
                    }
                },
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    useFlatPackage = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/FlatPackage.pack.deep.nested.kt",
            pathToLazyProperty = "imagevector/kt/lazy/FlatPackage.pack.deep.nested.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `different icon pack package`() {
        val icon = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.iconPack(
                iconName = parserOutput.iconName,
                packageName = DEFAULT_PACKAGE,
                iconPackPackage = "androidx.compose.material.icons",
                iconPackTree = buildTree("Icons") {
                    child("Filled")
                },
                imageVector = ImageVectorConfig(
                    outputFormat = outputFormat,
                    useFlatPackage = true,
                ),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/WithoutPath.pack.package.kt",
            pathToLazyProperty = "imagevector/kt/lazy/WithoutPath.pack.package.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `no package without icon pack`() {
        val icon = getResourcePath("imagevector/xml/ic_flat_package.xml").toIOPath()
        val parserOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = ImageVectorGeneratorConfig.simple(
                iconName = parserOutput.iconName,
                packageName = "",
                imageVector = ImageVectorConfig(outputFormat = outputFormat),
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "imagevector/kt/backing/NoPackage.kt",
            pathToLazyProperty = "imagevector/kt/lazy/NoPackage.kt",
        )
        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(output).isEqualTo(expected)
    }
}
