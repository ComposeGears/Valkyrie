package io.github.composegears.valkyrie.parser

import io.github.composegears.valkyrie.DEFAULT_CONFIG
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class IconParserTest {

    /**
     * add linear gradient icon
     * add radial gradient icon
     * color to uppercase
     */

    @Test
    fun `empty path xml`() {
        val testFile = File("src/test/resources/ic_without_path.xml")
        val output = IconParser.tryParse(file = testFile, config = DEFAULT_CONFIG)

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.ImageVector.Builder
            import androidx.compose.ui.unit.dp

            val ValkyrieIcons.WithoutPath: ImageVector
                get() {
                    if (_WithoutPath != null) {
                        return _WithoutPath!!
                    }
                    _WithoutPath = Builder(
                        name = "WithoutPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).build()

                    return _WithoutPath!!
                }

            private var _WithoutPath: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon parsing`() {
        val testFile = File("src/test/resources/ic_check.xml")
        val output = IconParser.tryParse(file = testFile, config = DEFAULT_CONFIG)

        println(output)

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.Color
            import androidx.compose.ui.graphics.SolidColor
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.ImageVector.Builder
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp

            val ValkyrieIcons.Check: ImageVector
                get() {
                    if (_Check != null) {
                        return _Check!!
                    }
                    _Check = Builder(
                        name = "Check",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).apply {
                        path(
                            fill = SolidColor(Color(0xFFe676ff)),
                        ) {
                            moveTo(6.75f, 12.127f)
                            lineTo(3.623f, 9.0f)
                            lineTo(2.558f, 10.057f)
                            lineTo(6.75f, 14.25f)
                            lineTo(15.75f, 5.25f)
                            lineTo(14.693f, 4.192f)
                            lineTo(6.75f, 12.127f)
                            close()
                        }
                    }
                    .build()

                    return _Check!!
                }

            private var _Check: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }
}