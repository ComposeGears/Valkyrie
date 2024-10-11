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
class CustomPackageTest {

    @Test
    fun `flat package without icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                outputFormat = outputFormat,
                useFlatPackage = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/FlatPackage.kt",
            pathToLazyProperty = "kt/lazy/FlatPackage.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `flat package with icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                outputFormat = outputFormat,
                useFlatPackage = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/FlatPackage.pack.kt",
            pathToLazyProperty = "kt/lazy/FlatPackage.pack.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `flat package with nested icon pack`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_flat_package.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packName = "ValkyrieIcons",
                nestedPackName = "Filled",
                outputFormat = outputFormat,
                useFlatPackage = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/FlatPackage.pack.nested.kt",
            pathToLazyProperty = "kt/lazy/FlatPackage.pack.nested.kt",
        )
        assertThat(output).isEqualTo(expected)
    }

    @Test
    fun `different icon pack package`(outputFormat: OutputFormat) {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                iconPackPackage = "androidx.compose.material.icons",
                useFlatPackage = true,
                packName = "Icons",
                nestedPackName = "Filled",
                outputFormat = outputFormat,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.pack.package.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.pack.package.kt",
        )
        assertThat(output).isEqualTo(expected)
    }
}
