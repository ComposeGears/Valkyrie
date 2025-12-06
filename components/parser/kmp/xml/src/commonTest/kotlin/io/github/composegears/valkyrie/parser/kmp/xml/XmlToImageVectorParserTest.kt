package io.github.composegears.valkyrie.parser.kmp.xml

import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.Close
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.LineTo
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode.MoveTo
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

    @Test
    fun parse_path_with_linear_gradient() {
        val vector = vectorWithGradient(
            """
            <path android:pathData="M0,0 L10,0 L10,10 L0,10 Z">
                <aapt:attr name="android:fillColor">
                    <gradient
                        android:type="linear"
                        android:startX="0"
                        android:startY="0"
                        android:endX="10"
                        android:endY="10"
                        android:startColor="#FF0000"
                        android:endColor="#0000FF"/>
                </aapt:attr>
            </path>
            """.trimIndent(),
        )

        assertEquals(
            expected = imageVector(
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.NonZero,
                        fill = IrFill.LinearGradient(
                            startX = 0f,
                            startY = 0f,
                            endX = 10f,
                            endY = 10f,
                            colorStops = mutableListOf(
                                IrFill.ColorStop(0f, IrColor(0xFFFF0000)),
                                IrFill.ColorStop(1f, IrColor(0xFF0000FF)),
                            ),
                        ),
                        fillAlpha = 1f,
                        paths = listOf(MoveTo(x=0f, y=0f), LineTo(x=10f, y=0f), LineTo(x=10f, y=10f), LineTo(x=0f, y=10f), Close),
                    ),
                ),
            ),
            actual = XmlToImageVectorParser.parse(vector),
        )
    }

    @Test
    fun parse_path_with_radial_gradient() {
        val vector = vectorWithGradient(
            """
            <path android:pathData="M0,0 L10,0 L10,10 L0,10 Z">
                <aapt:attr name="android:fillColor">
                    <gradient
                        android:type="radial"
                        android:centerX="5"
                        android:centerY="5"
                        android:gradientRadius="7.5"
                        android:startColor="#00FF00"
                        android:endColor="#FFFF00"/>
                </aapt:attr>
            </path>
            """.trimIndent(),
        )

        assertEquals(
            expected = imageVector(
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.NonZero,
                        fill = IrFill.RadialGradient(
                            centerX = 5f,
                            centerY = 5f,
                            radius = 7.5f,
                            colorStops = mutableListOf(
                                IrFill.ColorStop(0f, IrColor(0xFF00FF00)),
                                IrFill.ColorStop(1f, IrColor(0xFFFFFF00)),
                            ),
                        ),
                        fillAlpha = 1f,
                        paths = listOf(
                            MoveTo(x = 0f, y = 0f),
                            LineTo(x = 10f, y = 0f),
                            LineTo(x = 10f, y = 10f),
                            LineTo(x = 0f, y = 10f),
                            Close
                        ),
                    ),
                ),
            ),
            actual = XmlToImageVectorParser.parse(vector),
        )
    }

    @Test
    fun parse_path_with_linear_gradient_and_color_stops() {
        val vector = vectorWithGradient(
            """
            <path android:pathData="M0,0 L10,0 L10,10 L0,10 Z">
                <aapt:attr name="android:fillColor">
                    <gradient
                        android:type="linear"
                        android:startX="0"
                        android:startY="0"
                        android:endX="10"
                        android:endY="0">
                        <item android:color="#FF0000" android:offset="0"/>
                        <item android:color="#00FF00" android:offset="0.5"/>
                        <item android:color="#0000FF" android:offset="1"/>
                    </gradient>
                </aapt:attr>
            </path>
            """.trimIndent(),
        )

        assertEquals(
            expected = imageVector(
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.NonZero,
                        fill = IrFill.LinearGradient(
                            startX = 0f,
                            startY = 0f,
                            endX = 10f,
                            endY = 0f,
                            colorStops = mutableListOf(
                                IrFill.ColorStop(0f, IrColor(0xFFFF0000)),
                                IrFill.ColorStop(0.5f, IrColor(0xFF00FF00)),
                                IrFill.ColorStop(1f, IrColor(0xFF0000FF)),
                            ),
                        ),
                        fillAlpha = 1f,
                        paths = listOf(MoveTo(x=0f, y=0f), LineTo(x=10f, y=0f), LineTo(x=10f, y=10f), LineTo(x=0f, y=10f), Close),
                    ),
                ),
            ),
            actual = XmlToImageVectorParser.parse(vector),
        )
    }

    @Test
    fun parse_path_with_radial_gradient_and_color_stops() {
        val vector = vectorWithGradient(
            """
            <path android:pathData="M0,0 L10,0 L10,10 L0,10 Z">
                <aapt:attr name="android:fillColor">
                    <gradient
                        android:type="radial"
                        android:centerX="5"
                        android:centerY="5"
                        android:gradientRadius="10">
                        <item android:color="#FFFFFF" android:offset="0"/>
                        <item android:color="#888888" android:offset="0.7"/>
                        <item android:color="#000000" android:offset="1"/>
                    </gradient>
                </aapt:attr>
            </path>
            """.trimIndent(),
        )

        assertEquals(
            expected = imageVector(
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.NonZero,
                        fill = IrFill.RadialGradient(
                            centerX = 5f,
                            centerY = 5f,
                            radius = 10f,
                            colorStops = mutableListOf(
                                IrFill.ColorStop(0f, IrColor(0xFFFFFFFF)),
                                IrFill.ColorStop(0.7f, IrColor(0xFF888888)),
                                IrFill.ColorStop(1f, IrColor(0xFF000000)),
                            ),
                        ),
                        fillAlpha = 1f,
                        paths = listOf(MoveTo(x=0f, y=0f), LineTo(x=10f, y=0f), LineTo(x=10f, y=10f), LineTo(x=0f, y=10f), Close),
                    ),
                ),
            ),
            actual = XmlToImageVectorParser.parse(vector),
        )
    }

    @Test
    fun parse_multiple_paths_with_gradients() {
        val vector = vectorWithGradient(
            """
            <path android:pathData="M0,0 L5,0 L5,5 L0,5 Z">
                <aapt:attr name="android:fillColor">
                    <gradient
                        android:type="linear"
                        android:startX="0"
                        android:startY="0"
                        android:endX="5"
                        android:endY="0"
                        android:startColor="#FF0000"
                        android:endColor="#00FF00"/>
                </aapt:attr>
            </path>
            <path android:pathData="M6,0 L10,0 L10,5 L6,5 Z">
                <aapt:attr name="android:fillColor">
                    <gradient
                        android:type="radial"
                        android:centerX="8"
                        android:centerY="2.5"
                        android:gradientRadius="2.5"
                        android:startColor="#0000FF"
                        android:endColor="#FFFF00"/>
                </aapt:attr>
            </path>
            """.trimIndent(),
        )
        val result = XmlToImageVectorParser.parse(vector)

        assertEquals(2, result.nodes.size)
        val firstPath = result.nodes[0] as IrVectorNode.IrPath
        val secondPath = result.nodes[1] as IrVectorNode.IrPath

        assertEquals(
            expected = IrFill.LinearGradient(
                startX = 0f,
                startY = 0f,
                endX = 5f,
                endY = 0f,
                colorStops = mutableListOf(
                    IrFill.ColorStop(0f, IrColor(0xFFFF0000)),
                    IrFill.ColorStop(1f, IrColor(0xFF00FF00)),
                ),
            ),
            actual = firstPath.fill,
        )

        assertEquals(
            expected = IrFill.RadialGradient(
                centerX = 8f,
                centerY = 2.5f,
                radius = 2.5f,
                colorStops = mutableListOf(
                    IrFill.ColorStop(0f, IrColor(0xFF0000FF)),
                    IrFill.ColorStop(1f, IrColor(0xFFFFFF00)),
                ),
            ),
            actual = secondPath.fill,
        )
    }

    @Test
    fun parse_path_with_gradient_in_group() {
        val vector = vectorWithGradient(
            """
            <group android:name="gradientGroup">
                <path android:pathData="M0,0 L10,0 L10,10 L0,10 Z">
                    <aapt:attr name="android:fillColor">
                        <gradient
                            android:type="linear"
                            android:startX="0"
                            android:startY="0"
                            android:endX="10"
                            android:endY="10"
                            android:startColor="#FFFFFF"
                            android:endColor="#000000"/>
                    </aapt:attr>
                </path>
            </group>
            """.trimIndent(),
        )
        val result = XmlToImageVectorParser.parse(vector)

        assertEquals(1, result.nodes.size)
        val group = result.nodes[0] as IrVectorNode.IrGroup
        assertEquals("gradientGroup", group.name)
        assertEquals(1, group.nodes.size)

        val path = group.nodes[0] as IrVectorNode.IrPath
        assertEquals(
            expected = IrFill.LinearGradient(
                startX = 0f,
                startY = 0f,
                endX = 10f,
                endY = 10f,
                colorStops = mutableListOf(
                    IrFill.ColorStop(0f, IrColor(0xFFFFFFFF)),
                    IrFill.ColorStop(1f, IrColor(0xFF000000)),
                ),
            ),
            actual = path.fill,
        )
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

    private fun vectorWithGradient(
        content: String,
        width: String = "24dp",
        height: String = "24dp",
    ): String = buildString {
        appendLine(
            """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                    xmlns:aapt="http://schemas.android.com/aapt"
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
