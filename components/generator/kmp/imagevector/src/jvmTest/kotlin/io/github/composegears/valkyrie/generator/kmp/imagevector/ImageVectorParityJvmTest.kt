package io.github.composegears.valkyrie.generator.kmp.imagevector

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.generator.kmp.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.kmp.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourcePath
import org.junit.jupiter.api.Test

class ImageVectorParityJvmTest {

    @Test
    fun `basic generation parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_without_path.xml",
            configTransform = { createConfig(outputFormat = it) },
            backingExpected = "imagevector/kt/backing/WithoutPath.kt",
            lazyExpected = "imagevector/kt/lazy/WithoutPath.kt",
        )
    }

    @Test
    fun `nested pack parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_without_path.xml",
            configTransform = {
                createConfig(
                    packName = "ValkyrieIcons",
                    nestedPackName = "Colored",
                    outputFormat = it,
                )
            },
            backingExpected = "imagevector/kt/backing/WithoutPath.pack.nested.kt",
            lazyExpected = "imagevector/kt/lazy/WithoutPath.pack.nested.kt",
        )
    }

    @Test
    fun `trailing comma parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_fill_color_stroke.xml",
            configTransform = {
                createConfig(
                    packName = "ValkyrieIcons",
                    outputFormat = it,
                    addTrailingComma = true,
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
            configTransform = {
                createConfig(
                    packName = "ValkyrieIcons",
                    outputFormat = it,
                    usePathDataString = true,
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
            configTransform = { createConfig(outputFormat = it) },
            backingExpected = "imagevector/kt/backing/ClipPathGradient.kt",
            lazyExpected = "imagevector/kt/lazy/ClipPathGradient.kt",
        )
    }

    @Test
    fun `preview parity`() {
        assertParity(
            iconPath = "imagevector/xml/ic_without_path.xml",
            configTransform = {
                createConfig(
                    outputFormat = it,
                    packName = "ValkyrieIcons",
                    nestedPackName = "Filled",
                    generatePreview = true,
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
            configTransform = {
                createConfig(
                    outputFormat = it,
                    useComposeColors = true,
                    fullQualifiedImports = FullQualifiedImports(brush = true),
                )
            },
            backingExpected = "imagevector/kt/backing/FullQualified.brush.kt",
            lazyExpected = "imagevector/kt/lazy/FullQualified.brush.kt",
        )
    }

    private fun assertParity(
        iconPath: String,
        configTransform: (OutputFormat) -> ImageVectorGeneratorConfig,
        backingExpected: String,
        lazyExpected: String,
    ) {
        val parserOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Jvm,
            path = getResourcePath(iconPath).toIOPath(),
        )

        OutputFormat.entries.forEach { format ->
            val output = ImageVectorGenerator.convert(
                vector = parserOutput.irImageVector,
                iconName = parserOutput.iconName,
                config = configTransform(format),
            ).content

            val expected = format.toResourceText(
                pathToBackingProperty = backingExpected,
                pathToLazyProperty = lazyExpected,
            )

            assertThat(output).isEqualTo(expected)
        }
    }
}
