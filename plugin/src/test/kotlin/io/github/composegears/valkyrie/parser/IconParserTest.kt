package io.github.composegears.valkyrie.parser

import io.github.composegears.valkyrie.DEFAULT_CONFIG
import io.github.composegears.valkyrie.loadIcon
import org.junit.Assert.assertEquals
import org.junit.Test

class IconParserTest {

    /**
     * add linear gradient icon
     * add radial gradient icon
     * preview generation
     */

    @Test
    fun `empty path xml`() {
        val icon = loadIcon("ic_without_path.xml")
        val output = IconParser.tryParse(file = icon, config = DEFAULT_CONFIG)

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
    fun `icon only with path`() {
        val icon = loadIcon("ic_only_path.xml")
        val output = IconParser.tryParse(file = icon, config = DEFAULT_CONFIG)

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.ImageVector.Builder
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp

            val ValkyrieIcons.OnlyPath: ImageVector
                get() {
                    if (_OnlyPath != null) {
                        return _OnlyPath!!
                    }
                    _OnlyPath = Builder(
                        name = "OnlyPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).apply {
                        path {
                            moveTo(6.75f, 12.127f)
                            lineTo(3.623f, 9.0f)
                            lineTo(2.558f, 10.057f)
                            lineTo(6.75f, 14.25f)
                            lineTo(15.75f, 5.25f)
                            lineTo(14.693f, 4.192f)
                            lineTo(6.75f, 12.127f)
                            close()
                        }
                    }.build()

                    return _OnlyPath!!
                }

            private var _OnlyPath: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon with path and solid color`() {
        val icon = loadIcon("ic_fill_color_stroke.xml")
        val output = IconParser.tryParse(file = icon, config = DEFAULT_CONFIG)

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.Color
            import androidx.compose.ui.graphics.SolidColor
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.ImageVector.Builder
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp

            val ValkyrieIcons.FillColorStroke: ImageVector
                get() {
                    if (_FillColorStroke != null) {
                        return _FillColorStroke!!
                    }
                    _FillColorStroke = Builder(
                        name = "FillColorStroke",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).apply {
                        path(
                            fill = SolidColor(Color(0xFF232F34)),
                            strokeLineWidth = 1f
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
                    }.build()

                    return _FillColorStroke!!
                }

            private var _FillColorStroke: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon with all path params`() {
        val icon = loadIcon("ic_all_path_params.xml")
        val output = IconParser.tryParse(file = icon, config = DEFAULT_CONFIG)

        val expectedOutput = """
           package io.github.composegears.valkyrie.icons
           
           import androidx.compose.ui.graphics.Color
           import androidx.compose.ui.graphics.PathFillType
           import androidx.compose.ui.graphics.SolidColor
           import androidx.compose.ui.graphics.StrokeCap
           import androidx.compose.ui.graphics.StrokeJoin
           import androidx.compose.ui.graphics.vector.ImageVector
           import androidx.compose.ui.graphics.vector.ImageVector.Builder
           import androidx.compose.ui.graphics.vector.path
           import androidx.compose.ui.unit.dp

           val ValkyrieIcons.AllPathParams: ImageVector
               get() {
                   if (_AllPathParams != null) {
                       return _AllPathParams!!
                   }
                   _AllPathParams = Builder(
                       name = "AllPathParams",
                       defaultWidth = 24.dp,
                       defaultHeight = 24.dp,
                       viewportWidth = 18f,
                       viewportHeight = 18f
                   ).apply {
                       path(
                           fill = SolidColor(Color(0xFF232F34)),
                           fillAlpha = 0.5f,
                           stroke = SolidColor(Color(0xFF232F34)),
                           strokeAlpha = 0.5f,
                           strokeLineWidth = 1f,
                           strokeLineCap = StrokeCap.Round,
                           strokeLineJoin = StrokeJoin.Round,
                           strokeLineMiter = 3f,
                           pathFillType = PathFillType.EvenOdd
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
                   }.build()

                   return _AllPathParams!!
               }

           private var _AllPathParams: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon with several path`() {
        val icon = loadIcon("ic_several_path.xml")
        val output = IconParser.tryParse(file = icon, config = DEFAULT_CONFIG)

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.Color
            import androidx.compose.ui.graphics.SolidColor
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.ImageVector.Builder
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp

            val ValkyrieIcons.SeveralPath: ImageVector
                get() {
                    if (_SeveralPath != null) {
                        return _SeveralPath!!
                    }
                    _SeveralPath = Builder(
                        name = "SeveralPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).apply {
                        path(fill = SolidColor(Color(0xFFE676FF))) {
                            moveTo(6.75f, 12.127f)
                            lineTo(3.623f, 9.0f)
                            lineTo(2.558f, 10.057f)
                            lineTo(6.75f, 14.25f)
                            lineTo(15.75f, 5.25f)
                            lineTo(14.693f, 4.192f)
                            lineTo(6.75f, 12.127f)
                            close()
                        }
                        path(fill = SolidColor(Color(0xFFFF00FF))) {
                            moveTo(6.75f, 12.127f)
                            lineTo(3.623f, 9.0f)
                            lineTo(2.558f, 10.057f)
                            lineTo(6.75f, 14.25f)
                            lineTo(15.75f, 5.25f)
                            lineTo(14.693f, 4.192f)
                            lineTo(6.75f, 12.127f)
                            close()
                        }
                    }.build()

                    return _SeveralPath!!
                }

            private var _SeveralPath: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }
}