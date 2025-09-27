package io.github.composegears.valkyrie.sdk.generator.xml

import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode
import kotlin.test.Test
import kotlin.test.assertTrue

class IrToXmlGeneratorTest {

    @Test
    fun `parse ImageVector to valid VectorDrawable xml`() {
        val imageVector = imageVector(
            nodes = listOf(
                IrVectorNode.IrPath(
                    pathFillType = IrPathFillType.NonZero,
                    fill = IrFill.Color(IrColor(0xffffffff)),
                    fillAlpha = .5f,
                    paths = emptyList(),
                ),
            ),
        )

        val result = IrToXmlGenerator.generate(imageVector)

        with(result) {
            assertTrue(contains("android:fillColor=\"#FFFFFFFF\""))
            assertTrue(contains("android:fillAlpha=\"0.5\""))
            assertTrue(contains("android:pathData=\"\""))
            assertTrue(contains("android:width=\"24dp\""))
            assertTrue(contains("android:height=\"24dp\""))
            assertTrue(contains("android:viewportWidth=\"24.0\""))
            assertTrue(contains("android:viewportHeight=\"24.0\""))
        }
    }

    @Test
    fun `parse ImageVector with fillType evenOdd`() {
        val imageVector = imageVector(
            nodes = listOf(
                IrVectorNode.IrPath(
                    pathFillType = IrPathFillType.EvenOdd,
                    fill = IrFill.Color(IrColor(0xffffffff)),
                    fillAlpha = 1f,
                    paths = emptyList(),
                ),
            ),
        )

        val result = IrToXmlGenerator.generate(imageVector)

        assertTrue(result.contains("android:fillType=\"evenOdd\""))
        assertTrue(result.contains("android:fillColor=\"#FFFFFFFF\""))
    }

    @Test
    fun `parse ImageVector with group`() {
        val imageVector = imageVector(
            nodes = listOf(
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
        )

        val result = IrToXmlGenerator.generate(imageVector)

        with(result) {
            assertTrue(contains("android:name=\"hi\""))
            assertTrue(contains("android:pivotX=\"10.0\""))
            assertTrue(contains("android:pivotY=\"11.0\""))
            assertTrue(contains("android:rotation=\"15.0\""))
            assertTrue(contains("android:translateX=\"20.0\""))
            assertTrue(contains("android:translateY=\"21.0\""))
            assertTrue(contains("android:scaleX=\"30.0\""))
            assertTrue(contains("android:scaleY=\"31.0\""))
        }
    }

    @Test
    fun `parse ImageVector with stroke`() {
        val imageVector = IrImageVector(
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
        )

        val result = IrToXmlGenerator.generate(imageVector)

        with(result) {
            assertTrue(contains("android:strokeWidth=\"42\""))
            assertTrue(contains("android:strokeColor=\"#FFFFFFFF\""))
            assertTrue(contains("android:strokeAlpha=\"0.5\""))
            assertTrue(contains("android:strokeMiterLimit=\"2\""))
        }
    }

    @Test
    fun `parse ImageVector with integer dimensions`() {
        val imageVector = IrImageVector(
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
            nodes = emptyList(),
        )

        val result = IrToXmlGenerator.generate(imageVector)

        with(result) {
            assertTrue(contains("android:width=\"24dp\""))
            assertTrue(contains("android:height=\"24dp\""))
            assertTrue(contains("android:viewportWidth=\"24.0\""))
            assertTrue(contains("android:viewportHeight=\"24.0\""))
        }
    }

    @Test
    fun `parse ImageVector with fractional dimensions`() {
        val imageVector = IrImageVector(
            defaultWidth = 24.5f,
            defaultHeight = 25.75f,
            viewportWidth = 100.25f,
            viewportHeight = 200.125f,
            nodes = emptyList(),
        )

        val result = IrToXmlGenerator.generate(imageVector)

        with(result) {
            assertTrue(contains("android:width=\"24.5dp\""))
            assertTrue(contains("android:height=\"25.75dp\""))
            assertTrue(contains("android:viewportWidth=\"100.25\""))
            assertTrue(contains("android:viewportHeight=\"200.125\""))
        }
    }

    @Test
    fun `parse ImageVector with autoMirror true`() {
        val imageVector = IrImageVector(
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
            autoMirror = true,
            nodes = emptyList(),
        )

        val result = IrToXmlGenerator.generate(imageVector)

        assertTrue(result.contains("android:autoMirrored=\"true\""))
    }

    @Test
    fun `parse ImageVector with name`() {
        val imageVector = IrImageVector(
            name = "test_icon",
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
            nodes = emptyList(),
        )

        val result = IrToXmlGenerator.generate(imageVector)

        assertTrue(result.contains("android:name=\"test_icon\""))
    }

    private fun imageVector(nodes: List<IrVectorNode> = emptyList()): IrImageVector = IrImageVector(
        defaultWidth = 24f,
        defaultHeight = 24f,
        viewportWidth = 24f,
        viewportHeight = 24f,
        nodes = nodes,
    )
}
