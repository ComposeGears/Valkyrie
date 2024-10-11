package io.github.composegears.valkyrie.generator.imagevector

import app.cash.burst.Burst
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.imagevector.common.createConfig
import io.github.composegears.valkyrie.generator.imagevector.common.toResourceText
import io.github.composegears.valkyrie.parser.svgxml.SvgXmlParser
import org.junit.jupiter.api.Test

@Burst
class ExplicitModeTest(
    private val outputFormat: OutputFormat,
) {

    @Test
    fun `generation with explicit mode`() {
        val icon = getResourcePath("xml/ic_without_path.xml")
        val parserOutput = SvgXmlParser.toIrImageVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = createConfig(
                outputFormat = outputFormat,
                useExplicitMode = true,
            ),
        ).content

        val expected = outputFormat.toResourceText(
            pathToBackingProperty = "kt/backing/WithoutPath.explicit.kt",
            pathToLazyProperty = "kt/lazy/WithoutPath.explicit.kt",
        )
        assertThat(output).isEqualTo(expected)
    }
}
