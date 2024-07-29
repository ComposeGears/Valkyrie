package io.github.composegears.valkyrie.generator.imagevector.backingproperty

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.parser.IconParser
import org.junit.jupiter.api.Test

class PreviewGenerationTest {

    @Test
    fun `preview generation without icon pack`() {
        val icon = loadIcon("ic_without_path.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = ImageVectorGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                packName = "",
                nestedPackName = "",
                outputFormat = OutputFormat.BackingProperty,
                generatePreview = true,
            ),
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.foundation.Image
            import androidx.compose.foundation.layout.Box
            import androidx.compose.foundation.layout.padding
            import androidx.compose.runtime.Composable
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.tooling.preview.Preview
            import androidx.compose.ui.unit.dp
            import kotlin.Suppress

            val WithoutPath: ImageVector
                get() {
                    if (_WithoutPath != null) {
                        return _WithoutPath!!
                    }
                    _WithoutPath = ImageVector.Builder(
                        name = "WithoutPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).build()

                    return _WithoutPath!!
                }

            @Suppress("ObjectPropertyName")
            private var _WithoutPath: ImageVector? = null

            @Preview(showBackground = true)
            @Composable
            private fun WithoutPathPreview() {
                Box(modifier = Modifier.padding(12.dp)) {
                    Image(imageVector = WithoutPath, contentDescription = null)
                }
            }

        """.trimIndent()
        assertThat(output).isEqualTo(expectedOutput)
    }

    @Test
    fun `preview generation with icon pack`() {
        val icon = loadIcon("ic_without_path.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = ImageVectorGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                packName = "ValkyrieIcons",
                nestedPackName = "",
                outputFormat = OutputFormat.BackingProperty,
                generatePreview = true,
            ),
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.foundation.Image
            import androidx.compose.foundation.layout.Box
            import androidx.compose.foundation.layout.padding
            import androidx.compose.runtime.Composable
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.tooling.preview.Preview
            import androidx.compose.ui.unit.dp
            import kotlin.Suppress

            val ValkyrieIcons.WithoutPath: ImageVector
                get() {
                    if (_WithoutPath != null) {
                        return _WithoutPath!!
                    }
                    _WithoutPath = ImageVector.Builder(
                        name = "WithoutPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).build()

                    return _WithoutPath!!
                }

            @Suppress("ObjectPropertyName")
            private var _WithoutPath: ImageVector? = null

            @Preview(showBackground = true)
            @Composable
            private fun WithoutPathPreview() {
                Box(modifier = Modifier.padding(12.dp)) {
                    Image(imageVector = ValkyrieIcons.WithoutPath, contentDescription = null)
                }
            }

        """.trimIndent()
        assertThat(output).isEqualTo(expectedOutput)
    }

    @Test
    fun `preview generation with nested pack`() {
        val icon = loadIcon("ic_without_path.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = ImageVectorGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                packName = "ValkyrieIcons",
                nestedPackName = "Filled",
                outputFormat = OutputFormat.BackingProperty,
                generatePreview = true,
            ),
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons.filled

            import androidx.compose.foundation.Image
            import androidx.compose.foundation.layout.Box
            import androidx.compose.foundation.layout.padding
            import androidx.compose.runtime.Composable
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.tooling.preview.Preview
            import androidx.compose.ui.unit.dp
            import io.github.composegears.valkyrie.icons.ValkyrieIcons
            import kotlin.Suppress

            val ValkyrieIcons.Filled.WithoutPath: ImageVector
                get() {
                    if (_WithoutPath != null) {
                        return _WithoutPath!!
                    }
                    _WithoutPath = ImageVector.Builder(
                        name = "Filled.WithoutPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).build()

                    return _WithoutPath!!
                }

            @Suppress("ObjectPropertyName")
            private var _WithoutPath: ImageVector? = null

            @Preview(showBackground = true)
            @Composable
            private fun WithoutPathPreview() {
                Box(modifier = Modifier.padding(12.dp)) {
                    Image(imageVector = ValkyrieIcons.Filled.WithoutPath, contentDescription = null)
                }
            }

        """.trimIndent()
        assertThat(output).isEqualTo(expectedOutput)
    }
}
