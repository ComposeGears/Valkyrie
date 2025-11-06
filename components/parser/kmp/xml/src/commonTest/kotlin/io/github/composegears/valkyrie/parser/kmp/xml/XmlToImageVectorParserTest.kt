package io.github.composegears.valkyrie.parser.kmp.xml

import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class XmlToImageVectorParserTest {

    @Test
    fun `invalid vector throws exception`() {
        assertFails { XmlToImageVectorParser.parse("INVALID VECTOR") }
    }

    @Test
    fun parse_valid_VectorDrawable_xml_to_ImageVector() {
        val vector = vector(
            """
                <path
                    android:fillColor="@android:color/white"
                    android:fillAlpha="0.5"
                    android:pathData=""/>
            """.trimIndent(),
        )
        assertEquals(
            expected = imageVector(
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.NonZero,
                        fill = IrFill.Color(IrColor(0xffffffff)),
                        fillAlpha = .5f,
                        paths = emptyList(),
                    ),
                ),
            ),
            actual = XmlToImageVectorParser.parse(vector),
        )
    }

    @Test
    fun parse_valid_VectorDrawable_with_fillType_evenOdd_to_ImageVector() {
        val v = vector(
            """
            <path
             android:fillType="evenOdd"
             android:fillColor="@android:color/white"
             android:pathData=""/>
            """.trimIndent(),
        )
        assertEquals(
            expected = imageVector(
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.EvenOdd,
                        fill = IrFill.Color(IrColor(0xffffffff)),
                        fillAlpha = 1f,
                        paths = emptyList(),
                    ),
                ),
            ),
            actual = XmlToImageVectorParser.parse(v),
        )
    }

    @Test
    fun parse_drawable_with_group() {
        val vector = vector(
            """
                <group
                    android:name="hi"
                    android:pivotX="10.0"
                    android:pivotY="11.0"
                    android:rotation="15.0"
                    android:translateX="20.0"
                    android:translateY="21.0"
                    android:scaleX="30.0"
                    android:scaleY="31.0"
                />
            """.trimIndent(),
        )
        val result = XmlToImageVectorParser.parse(vector)
        assertEquals(
            expected = listOf(
                IrVectorNode.IrGroup(
                    name = "hi",
                    rotate = 15f,
                    pivotX = 10f,
                    pivotY = 11f,
                    translationX = 20f,
                    translationY = 21f,
                    scaleX = 30f,
                    scaleY = 31f,
                    clipPathData = mutableListOf(),
                    nodes = mutableListOf(),
                ),
            ),
            actual = result.nodes,
        )
    }

    @Test
    fun parse_path_with_stroke() {
        val vector = vector(
            """
             <path
             android:pathData=""
             android:strokeWidth="42"
             android:strokeLineCap="butt"
             android:strokeLineJoin="bevel"
             android:strokeColor="#fff"
             android:strokeAlpha=".5"
             android:strokeMiterLimit="2"
             />
            """.trimIndent(),
        )
        val result = XmlToImageVectorParser.parse(vector)
        assertEquals(
            actual = result,
            expected = IrImageVector(
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                nodes = listOf(
                    IrVectorNode.IrPath(
                        paths = emptyList(),
                        fillAlpha = 1f,
                        pathFillType = IrPathFillType.NonZero,
                        strokeAlpha = .5f,
                        strokeLineWidth = 42f,
                        strokeLineCap = IrStrokeLineCap.Butt,
                        strokeLineJoin = IrStrokeLineJoin.Bevel,
                        strokeLineMiter = 2f,
                        stroke = IrStroke.Color(IrColor(0xffffffff)),
                    ),
                ),
            ),
        )
    }

    @Test
    fun parse_int_width_vector() {
        assertEquals(
            actual = XmlToImageVectorParser.parse(vector("", width = "24")),
            expected = IrImageVector(
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                nodes = emptyList(),
            ),
        )
    }

    @Test
    fun parse_invalid_width_vector_throws_IllegalArgumentException() {
        assertFailsWith<IllegalArgumentException> {
            XmlToImageVectorParser.parse(vector("", width = "r4nd0m"))
        }
    }

    private fun vector(
        content: String,
        width: String = "24dp",
        height: String = "24dp",
    ): String = buildString {
        appendLine(
            """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                    android:width="$width"
                    android:height="$height"
                    android:viewportWidth="24"
                    android:viewportHeight="24">
            """.trimIndent(),
        )
        appendLine(content)
        appendLine("</vector>")
    }

    private fun imageVector(nodes: List<IrVectorNode> = emptyList()): IrImageVector = IrImageVector(
        defaultWidth = 24f,
        defaultHeight = 24f,
        viewportWidth = 24f,
        viewportHeight = 24f,
        nodes = nodes,
    )
}
