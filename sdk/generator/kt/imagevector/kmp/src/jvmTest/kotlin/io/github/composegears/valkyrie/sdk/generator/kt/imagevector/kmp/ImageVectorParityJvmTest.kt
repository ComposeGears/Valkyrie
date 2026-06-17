package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.FullyQualifiedImports
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
class ImageVectorParityJvmTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `basic generation parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_without_path.xml",
            configTransform = { iconName ->
                ImageVectorGeneratorConfig.simple(
                    iconName = iconName,
                    packageName = DEFAULT_PACKAGE,
                    imageVector = ImageVectorConfig(outputFormat = outputFormat),
                )
            },
            backingExpected = "imagevector/kt/backing/WithoutPath.kt",
            lazyExpected = "imagevector/kt/lazy/WithoutPath.kt",
        )
    }

    @Test
    fun `nested pack parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_without_path.xml",
            configTransform = { iconName ->
                ImageVectorGeneratorConfig.iconPack(
                    iconName = iconName,
                    packageName = DEFAULT_PACKAGE,
                    iconPackTree = buildTree("ValkyrieIcons") {
                        child("Colored")
                    },
                    imageVector = ImageVectorConfig(outputFormat = outputFormat),
                )
            },
            backingExpected = "imagevector/kt/backing/WithoutPath.pack.nested.kt",
            lazyExpected = "imagevector/kt/lazy/WithoutPath.pack.nested.kt",
        )
    }

    @Test
    fun `deep nested pack parity`() {
        val parserOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Jvm,
            path = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath(),
        )
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
                imageVector = ImageVectorConfig(outputFormat = outputFormat),
            ),
        ).content

        assertThat(output).contains("val ValkyrieIcons.Material.Rounded.WithoutPath: ImageVector")
        assertThat(output).contains("""name = "Rounded.WithoutPath"""")
        assertThat(output).contains("package io.github.composegears.valkyrie.icons.material.rounded")
    }

    @Test
    fun `trailing comma parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_fill_color_stroke.xml",
            configTransform = { iconName ->
                ImageVectorGeneratorConfig.iconPack(
                    iconName = iconName,
                    packageName = DEFAULT_PACKAGE,
                    iconPackTree = buildTree("ValkyrieIcons"),
                    imageVector = ImageVectorConfig(
                        outputFormat = outputFormat,
                        addTrailingComma = true,
                    ),
                )
            },
            backingExpected = "imagevector/kt/backing/FillColorStroke.trailing.kt",
            lazyExpected = "imagevector/kt/lazy/FillColorStroke.trailing.kt",
        )
    }

    @Test
    fun `pathData mode parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_only_path.xml",
            configTransform = { iconName ->
                ImageVectorGeneratorConfig.iconPack(
                    iconName = iconName,
                    packageName = DEFAULT_PACKAGE,
                    iconPackTree = buildTree("ValkyrieIcons"),
                    imageVector = ImageVectorConfig(
                        outputFormat = outputFormat,
                        usePathDataString = true,
                    ),
                )
            },
            backingExpected = "imagevector/kt/backing/OnlyPathWithPathData.kt",
            lazyExpected = "imagevector/kt/lazy/OnlyPathWithPathData.kt",
        )
    }

    @Test
    fun `clip path builder parity`() {
        assertParity(
            iconPath = "imagevector/svg/ic_clip_path_gradient.svg",
            configTransform = { iconName ->
                ImageVectorGeneratorConfig.simple(
                    iconName = iconName,
                    packageName = DEFAULT_PACKAGE,
                    imageVector = ImageVectorConfig(outputFormat = outputFormat),
                )
            },
            backingExpected = "imagevector/kt/backing/ClipPathGradient.kt",
            lazyExpected = "imagevector/kt/lazy/ClipPathGradient.kt",
        )
    }

    @Test
    fun `preview parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_without_path.xml",
            configTransform = { iconName ->
                ImageVectorGeneratorConfig.iconPack(
                    iconName = iconName,
                    packageName = DEFAULT_PACKAGE,
                    iconPackTree = buildTree("ValkyrieIcons") {
                        child("Filled")
                    },
                    imageVector = ImageVectorConfig(
                        outputFormat = outputFormat,
                        generatePreview = true,
                    ),
                )
            },
            backingExpected = "imagevector/kt/backing/WithoutPath.pack.nested.preview.androidx.kt",
            lazyExpected = "imagevector/kt/lazy/WithoutPath.pack.nested.preview.androidx.kt",
        )
    }

    @Test
    fun `full qualified imports parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_full_qualified.xml",
            configTransform = { iconName ->
                ImageVectorGeneratorConfig.simple(
                    iconName = iconName,
                    packageName = DEFAULT_PACKAGE,
                    imageVector = ImageVectorConfig(outputFormat = outputFormat),
                    fullyQualifiedImports = FullyQualifiedImports(brush = true),
                )
            },
            backingExpected = "imagevector/kt/backing/FullQualified.brush.kt",
            lazyExpected = "imagevector/kt/lazy/FullQualified.brush.kt",
        )
    }

    @Test
    fun `deep nested generates correct receiver and builder name`() {
        val parserOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Jvm,
            path = getResourcePath("imagevector/xml/ic_flat_package.xml").toIOPath(),
        )
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

        assertThat(output).contains("val ValkyrieIcons.Material.Rounded.FlatPackage: ImageVector")
        assertThat(output).contains("""name = "Rounded.FlatPackage"""")
    }

    @Test
    fun `suppress unused receiver parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_without_path.xml",
            configTransform = { iconName ->
                ImageVectorGeneratorConfig.iconPack(
                    iconName = iconName,
                    packageName = DEFAULT_PACKAGE,
                    iconPackTree = buildTree("ValkyrieIcons"),
                    imageVector = ImageVectorConfig(
                        outputFormat = outputFormat,
                        suppressUnusedReceiverWarning = true,
                    ),
                )
            },
            backingExpected = "imagevector/kt/backing/WithoutPath.pack.suppress_receiver.kt",
            lazyExpected = "imagevector/kt/lazy/WithoutPath.pack.suppress_receiver.kt",
        )
    }

    private fun assertParity(
        iconPath: String,
        configTransform: (iconName: String) -> ImageVectorGeneratorConfig,
        backingExpected: String,
        lazyExpected: String,
    ) {
        val parserOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Jvm,
            path = getResourcePath(iconPath).toIOPath(),
        )

        val output = ImageVectorGenerator.convert(
            vector = parserOutput.irImageVector,
            config = configTransform(parserOutput.iconName),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = backingExpected,
            pathToLazyProperty = lazyExpected,
        )

        assertThat(output).isEqualTo(expected)
    }
}
