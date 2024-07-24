package io.github.composegears.valkyrie.generator.imagevector

import io.github.composegears.valkyrie.parser.IconParser
import org.junit.Assert.assertEquals
import org.junit.Test

class XmlParserTest {

    @Test
    fun `generation without icon pack`() {
        val icon = loadIcon("ic_without_path.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = ImageVectorGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                packName = "",
                nestedPackName = "",
                generatePreview = false,
            ),
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.vector.ImageVector
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

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `generation with nested icon pack`() {
        val icon = loadIcon("ic_without_path.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = ImageVectorGeneratorConfig(
                packageName = "io.github.composegears.valkyrie.icons",
                packName = "ValkyrieIcons",
                nestedPackName = "Colored",
                generatePreview = false,
            ),
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons.colored

            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.unit.dp
            import io.github.composegears.valkyrie.icons.ValkyrieIcons
            import kotlin.Suppress

            val ValkyrieIcons.Colored.WithoutPath: ImageVector
                get() {
                    if (_WithoutPath != null) {
                        return _WithoutPath!!
                    }
                    _WithoutPath = ImageVector.Builder(
                        name = "Colored.WithoutPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).build()

                    return _WithoutPath!!
                }

            @Suppress("ObjectPropertyName")
            private var _WithoutPath: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `empty path xml`() {
        val icon = loadIcon("ic_without_path.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = DEFAULT_CONFIG,
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.vector.ImageVector
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

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon only with path`() {
        val icon = loadIcon("ic_only_path.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = DEFAULT_CONFIG,
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp
            import kotlin.Suppress

            val ValkyrieIcons.OnlyPath: ImageVector
                get() {
                    if (_OnlyPath != null) {
                        return _OnlyPath!!
                    }
                    _OnlyPath = ImageVector.Builder(
                        name = "OnlyPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).apply {
                        path {
                            moveTo(6.75f, 12.127f)
                            lineTo(3.623f, 9f)
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

            @Suppress("ObjectPropertyName")
            private var _OnlyPath: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon with path and solid color`() {
        val icon = loadIcon("ic_fill_color_stroke.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = DEFAULT_CONFIG,
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.Color
            import androidx.compose.ui.graphics.SolidColor
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp
            import kotlin.Suppress

            val ValkyrieIcons.FillColorStroke: ImageVector
                get() {
                    if (_FillColorStroke != null) {
                        return _FillColorStroke!!
                    }
                    _FillColorStroke = ImageVector.Builder(
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
                            lineTo(3.623f, 9f)
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

            @Suppress("ObjectPropertyName")
            private var _FillColorStroke: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon with all path params`() {
        val icon = loadIcon("ic_all_path_params.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = DEFAULT_CONFIG,
        ).content

        val expectedOutput = """
           package io.github.composegears.valkyrie.icons

           import androidx.compose.ui.graphics.Color
           import androidx.compose.ui.graphics.PathFillType
           import androidx.compose.ui.graphics.SolidColor
           import androidx.compose.ui.graphics.StrokeCap
           import androidx.compose.ui.graphics.StrokeJoin
           import androidx.compose.ui.graphics.vector.ImageVector
           import androidx.compose.ui.graphics.vector.path
           import androidx.compose.ui.unit.dp
           import kotlin.Suppress

           val ValkyrieIcons.AllPathParams: ImageVector
               get() {
                   if (_AllPathParams != null) {
                       return _AllPathParams!!
                   }
                   _AllPathParams = ImageVector.Builder(
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
                           lineTo(3.623f, 9f)
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

           @Suppress("ObjectPropertyName")
           private var _AllPathParams: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon with several path`() {
        val icon = loadIcon("ic_several_path.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = DEFAULT_CONFIG,
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.Color
            import androidx.compose.ui.graphics.SolidColor
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp
            import kotlin.Suppress

            val ValkyrieIcons.SeveralPath: ImageVector
                get() {
                    if (_SeveralPath != null) {
                        return _SeveralPath!!
                    }
                    _SeveralPath = ImageVector.Builder(
                        name = "SeveralPath",
                        defaultWidth = 24.dp,
                        defaultHeight = 24.dp,
                        viewportWidth = 18f,
                        viewportHeight = 18f
                    ).apply {
                        path(fill = SolidColor(Color(0xFFE676FF))) {
                            moveTo(6.75f, 12.127f)
                            lineTo(3.623f, 9f)
                            lineTo(2.558f, 10.057f)
                            lineTo(6.75f, 14.25f)
                            lineTo(15.75f, 5.25f)
                            lineTo(14.693f, 4.192f)
                            lineTo(6.75f, 12.127f)
                            close()
                        }
                        path(fill = SolidColor(Color(0xFFFF00FF))) {
                            moveTo(6.75f, 12.127f)
                            lineTo(3.623f, 9f)
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

            @Suppress("ObjectPropertyName")
            private var _SeveralPath: ImageVector? = null

        """.trimIndent()
        assertEquals(expectedOutput, output)
    }

    @Test
    fun `icon with transparent fill color`() {
        val icon = loadIcon("ic_transparent_fill_color.xml")
        val parserOutput = IconParser.toVector(icon)
        val output = ImageVectorGenerator.convert(
            vector = parserOutput.vector,
            kotlinName = parserOutput.kotlinName,
            config = DEFAULT_CONFIG,
        ).content

        val expectedOutput = """
            package io.github.composegears.valkyrie.icons

            import androidx.compose.ui.graphics.Color
            import androidx.compose.ui.graphics.SolidColor
            import androidx.compose.ui.graphics.StrokeJoin
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp
            import kotlin.Suppress

            val ValkyrieIcons.TransparentFillColor: ImageVector
                get() {
                    if (_TransparentFillColor != null) {
                        return _TransparentFillColor!!
                    }
                    _TransparentFillColor = ImageVector.Builder(
                        name = "TransparentFillColor",
                        defaultWidth = 192.dp,
                        defaultHeight = 192.dp,
                        viewportWidth = 192f,
                        viewportHeight = 192f
                    ).apply {
                        path(
                            stroke = SolidColor(Color(0xFF000000)),
                            strokeLineWidth = 12f,
                            strokeLineJoin = StrokeJoin.Round
                        ) {
                            moveTo(22f, 57.26f)
                            verticalLineToRelative(84.74f)
                            curveToRelative(0f, 5.52f, 4.48f, 10f, 10f, 10f)
                            horizontalLineToRelative(18f)
                            curveToRelative(3.31f, 0f, 6f, -2.69f, 6f, -6f)
                            verticalLineTo(95.06f)
                            lineToRelative(40f, 30.28f)
                            lineToRelative(40f, -30.28f)
                            verticalLineToRelative(50.94f)
                            curveToRelative(0f, 3.31f, 2.69f, 6f, 6f, 6f)
                            horizontalLineToRelative(18f)
                            curveToRelative(5.52f, 0f, 10f, -4.48f, 10f, -10f)
                            verticalLineTo(57.26f)
                            curveToRelative(0f, -13.23f, -15.15f, -20.75f, -25.68f, -12.74f)
                            lineTo(96f, 81.26f)
                            lineTo(47.68f, 44.53f)
                            curveToRelative(-10.52f, -8.01f, -25.68f, 3.48f, -25.68f, 12.73f)
                            close()
                        }
                    }.build()

                    return _TransparentFillColor!!
                }

            @Suppress("ObjectPropertyName")
            private var _TransparentFillColor: ImageVector? = null

        """.trimIndent()

        assertEquals(expectedOutput, output)
    }
}
