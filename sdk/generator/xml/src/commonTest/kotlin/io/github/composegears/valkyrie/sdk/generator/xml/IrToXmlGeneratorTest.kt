package io.github.composegears.valkyrie.sdk.generator.xml

import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrPathNode
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode
import io.github.composegears.valkyrie.parser.kmp.xml.XmlToImageVectorParser
import io.github.composegears.valkyrie.resource.loader.ResourceLoader.getResourceText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
            validateVectorAttributes(xml = this, size = 24, viewportSize = 24f)
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
        validateVectorAttributes(xml = result, size = 24, viewportSize = 24f)
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

    @Test
    fun `parse ImageVector with clip path in group`() {
        val imageVector = imageVector(
            nodes = listOf(
                IrVectorNode.IrGroup(
                    name = "clipped_group",
                    clipPathData = mutableListOf(
                        IrPathNode.MoveTo(12f, 2f),
                        IrPathNode.LineTo(22f, 12f),
                        IrPathNode.LineTo(12f, 22f),
                        IrPathNode.LineTo(2f, 12f),
                        IrPathNode.Close,
                    ),
                    nodes = mutableListOf(
                        IrVectorNode.IrPath(
                            fill = IrFill.Color(IrColor(0xffff0000)),
                            paths = emptyList(),
                        ),
                    ),
                ),
            ),
        )

        val result = IrToXmlGenerator.generate(imageVector)

        with(result) {
            assertTrue(contains("<group"))
            assertTrue(contains("</group>"))
            assertTrue(contains("<clip-path"))
            assertTrue(contains("android:name=\"clipped_group\""))
            assertTrue(contains("android:pathData=\"M 12 2 L 22 12 L 12 22 L 2 12 Z\""))
            assertTrue(contains("android:fillColor=\"#FFFF0000\""))
        }
    }

    @Test
    fun `round trip test for ic_only_path xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_only_path.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(1, pathCount, "Should contain exactly 1 path element")

        val expectedPathData =
            "M 6.75 12.127 L 3.623 9 L 2.558 10.057 L 6.75 14.25 L 15.75 5.25 L 14.693 4.192 L 6.75 12.127 Z"
        assertTrue(generatedXml.contains(expectedPathData))
    }

    @Test
    fun `round trip test for ic_all_path_params xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_all_path_params.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f, autoMirrored = true)

        assertTrue(generatedXml.contains("android:name=\"path_name\""))
        assertTrue(generatedXml.contains("android:strokeWidth=\"1\""))
        assertTrue(generatedXml.contains("android:fillAlpha=\"0.5\""))
        assertTrue(generatedXml.contains("android:strokeColor=\"#FF232F34\"")) // #232F34 becomes #FF232F34
        assertTrue(generatedXml.contains("android:strokeAlpha=\"0.5\""))
        assertTrue(generatedXml.contains("android:strokeLineCap=\"round\""))
        assertTrue(generatedXml.contains("android:strokeLineJoin=\"round\""))
        assertTrue(generatedXml.contains("android:strokeMiterLimit=\"3\""))
        assertTrue(generatedXml.contains("android:fillType=\"evenOdd\""))
        assertTrue(generatedXml.contains("android:fillColor=\"#FF232F34\""))

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(1, pathCount, "Should contain exactly 1 path element")
        val expectedPathData =
            "M 6.75 12.127 L 3.623 9 L 2.558 10.057 L 6.75 14.25 L 15.75 5.25 L 14.693 4.192 L 6.75 12.127 Z"
        assertTrue(generatedXml.contains(expectedPathData))
    }

    @Test
    fun `round trip test for ic_all_group_params xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_all_group_params.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 24f)

        assertTrue(generatedXml.contains("<group"))
        assertTrue(generatedXml.contains("</group>"))

        val groupCount = generatedXml.split("<group").lastIndex
        assertEquals(1, groupCount, "Should contain exactly 1 group")
        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(2, pathCount, "Should contain exactly 2 paths")

        with(generatedXml) {
            assertTrue(contains("android:name=\"group\""))
            assertTrue(contains("android:rotation=\"15.0\""))
            assertTrue(contains("android:pivotX=\"10.0\""))
            assertTrue(contains("android:pivotY=\"10.0\""))
            assertTrue(contains("android:scaleX=\"0.8\""))
            assertTrue(contains("android:scaleY=\"0.8\""))
            assertTrue(contains("android:translateX=\"6.0\""))
            assertTrue(contains("android:translateY=\"1.0\""))

            assertTrue(contains("android:fillColor=\"#FF000000\""))
        }

        assertTrue(generatedXml.contains("android:fillAlpha=\"0.3\""))
    }

    @Test
    fun `round trip test for ic_fill_color_stroke xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_fill_color_stroke.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(1, pathCount, "Should contain exactly 1 path element")

        assertTrue(generatedXml.contains("android:strokeWidth=\"1\""))
        assertTrue(generatedXml.contains("android:fillColor=\"#FF232F34\"")) // #232F34 becomes #FF232F34

        val expectedPathData =
            "M 6.75 12.127 L 3.623 9 L 2.558 10.057 L 6.75 14.25 L 15.75 5.25 L 14.693 4.192 L 6.75 12.127 Z"
        assertTrue(generatedXml.contains(expectedPathData))
    }

    @Test
    fun `round trip test for ic_flat_package xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_flat_package.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(0, pathCount, "Should contain no path elements - original is empty")

        val groupCount = generatedXml.split("<group").lastIndex
        assertEquals(0, groupCount, "Should contain no group elements - original is empty")
    }

    @Test
    fun `round trip test for ic_several_path xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_several_path.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(2, pathCount, "Should contain exactly 2 path elements")

        // Both paths have the same pathData but different colors
        val expectedPathData =
            "M 6.75 12.127 L 3.623 9 L 2.558 10.057 L 6.75 14.25 L 15.75 5.25 L 14.693 4.192 L 6.75 12.127 Z"
        assertTrue(generatedXml.contains(expectedPathData))

        assertTrue(generatedXml.contains("android:fillColor=\"#FFE676FF\"")) // #e676ff becomes #FFE676FF
        assertTrue(generatedXml.contains("android:fillColor=\"#FFFF00FF\"")) // #ff00ff becomes #FFFF00FF
    }

    @Test
    fun `round trip test for ic_transparent_fill_color xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_transparent_fill_color.xml")

        validateVectorAttributes(xml = generatedXml, size = 192, viewportSize = 192f)

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(1, pathCount, "Should contain exactly 1 path element")

        assertTrue(generatedXml.contains("android:strokeColor=\"#FF000000\""))
        assertTrue(generatedXml.contains("android:strokeWidth=\"12\""))
        assertTrue(generatedXml.contains("android:strokeLineJoin=\"round\""))

        val expectedPathData =
            "M 22 57.26 v 84.74 c 0 5.52 4.48 10 10 10 h 18 c 3.31 0 6 -2.69 6 -6 V 95.06 l 40 30.28 l 40 -30.28 v 50.94 c 0 3.31 2.69 6 6 6 h 18 c 5.52 0 10 -4.48 10 -10 V 57.26 c 0 -13.23 -15.15 -20.75 -25.68 -12.74 L 96 81.26 L 47.68 44.53 c -10.52 -8.01 -25.68 3.48 -25.68 12.73 Z"
        assertTrue(generatedXml.contains(expectedPathData))
    }

    @Test
    fun `round trip test for ic_without_path xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_without_path.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(0, pathCount, "Should contain no path elements - original is empty")

        val groupCount = generatedXml.split("<group").lastIndex
        assertEquals(0, groupCount, "Should contain no group elements - original is empty")
    }

    @Test
    fun `round trip test for icon_with_named_args xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/icon_with_named_args.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 24f)

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(1, pathCount, "Should contain exactly 1 path element")

        assertTrue(generatedXml.contains("android:fillColor=\"#FF232F34\""))

        assertTrue(generatedXml.contains("M 21"))
    }

    @Test
    fun `round trip test for icon_with_shorthand_color xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/icon_with_shorthand_color.xml")

        validateVectorAttributes(xml = generatedXml, size = 48, viewportSize = 512f)

        assertTrue(generatedXml.contains("<group"))
        assertTrue(generatedXml.contains("</group>"))

        val groupCount = generatedXml.split("<group").lastIndex
        assertEquals(1, groupCount, "Should contain exactly 1 group")
        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(2, pathCount, "Should contain exactly 2 paths")

        assertTrue(generatedXml.contains("android:fillColor=\"#FFD80027\"")) // #d80027 becomes #FFD80027
        assertTrue(generatedXml.contains("android:fillColor=\"#FFEEEEEE\"")) // #eee becomes #FFEEEEEE

        assertTrue(generatedXml.contains("M 0 0 h 512 v 167 l -23.2 89.7 L 512 345 v 167 H 0 V 345 l 29.4 -89 L 0 167 Z")) // First path
        assertTrue(generatedXml.contains("M 0 167 h 512 v 178")) // Second path
    }

    @Test
    fun `round trip test for ic_brush xml - GRADIENTS NOT FULLY SUPPORTED`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_brush.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        // TODO: Original has 3 paths with complex linear and radial gradients
        // Note: The gradient information will be lost in current parsing/generation
        // This needs to be addressed when full gradient support is implemented
        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(pathCount, 3, "Should contain 3 path elements")

        assertTrue(generatedXml.contains("M 2 2 L 6 2 L 6 6 L 2 6 Z"))
        assertTrue(generatedXml.contains("M 6 2 L 10 2 L 10 6 L 6 6 Z"))
        assertTrue(generatedXml.contains("M 63.6 118.8 c -27.9 0 -58 -17.5 -58 -55.9 S 35.7 7 63.6 7 c 15.5 0 29.8 5.1 40.4 14.4 c 11.5 10.2 17.6 24.6 17.6 41.5 s -6.1 31.2 -17.6 41.4 C 93.4 113.6 79 118.8 63.6 118.8 Z"))
    }

    @Test
    fun `round trip test for ic_compose_color xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_compose_color.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(16, pathCount, "Should contain exactly 16 path elements")

        with(generatedXml) {
            assertTrue(contains("android:fillColor=\"#FF000000\""))
            assertTrue(contains("android:fillColor=\"#FF444444\""))
            assertTrue(contains("android:fillColor=\"#FF888888\""))
            assertTrue(contains("android:fillColor=\"#FFCCCCCC\""))
            assertTrue(contains("android:fillColor=\"#FFFFFFFF\""))
            assertTrue(contains("android:fillColor=\"#FFFF0000\""))
            assertTrue(contains("android:fillColor=\"#FF00FF00\""))
            assertTrue(contains("android:fillColor=\"#FF0000FF\""))
            assertTrue(contains("android:fillColor=\"#FFFFFF00\""))
            assertTrue(contains("android:fillColor=\"#FF00FFFF\""))
            assertTrue(contains("android:fillColor=\"#FFFF00FF\""))
            assertTrue(contains("android:fillColor=\"#80FF0000\""))
            assertTrue(contains("android:fillColor=\"#8000FF00\""))
            assertTrue(contains("android:fillColor=\"#800000FF\""))
            assertTrue(contains("android:fillColor=\"#40000000\""))
            assertTrue(contains("android:fillColor=\"#BFFFFFFF\""))
        }
    }

    @Test
    fun `round trip test for ic_full_qualified xml - GRADIENTS NOT FULLY SUPPORTED`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_full_qualified.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        // TODO: Original has 3 paths with complex gradients that aren't fully parsed
        // For now, we just verify basic structure
        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(pathCount, 3, "Should contain exactly 3 path elements")
    }

    @Test
    fun `round trip test for ic_compose_color_linear_gradient xml - GRADIENTS NOT FULLY SUPPORTED`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_compose_color_linear_gradient.xml")

        // Basic structure validation only
        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        // TODO: Original has 16 paths with linear gradients - needs gradient support
        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(pathCount, 16, "Should contain exactly 16 path elements")
    }

    @Test
    fun `round trip test for ic_compose_color_radial_gradient xml - GRADIENTS NOT FULLY SUPPORTED`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_compose_color_radial_gradient.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 18f)

        // TODO: Original has 16 paths with radial gradients - needs gradient support
        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(pathCount, 16, "Should contain exactly 16 path elements")
    }

    @Test
    fun `round trip test for ic_linear_radial_gradient_with_offset xml - GRADIENTS NOT FULLY SUPPORTED`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_linear_radial_gradient_with_offset.xml")

        validateVectorAttributes(xml = generatedXml, size = 128, viewportSize = 128f)

        // TODO: Original has complex gradients with offset items - needs gradient support
        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(7, pathCount, "Should contain 7 paths")

        assertTrue(generatedXml.contains("<group"))
        val clipPathCount = generatedXml.split("<clip-path").lastIndex
        assertEquals(1, clipPathCount, "Should contain exactly 1 clip-path element")

        // Verify the clip-path data is preserved
        val expectedClipPathData =
            "M 66.8 76.5 c -10.89 3.76 -22.1 6.51 -33.5 8.2 c -1.92 0.25 -3.27 2.02 -3.02 3.94 c 0.06 0.44 0.2 0.87 0.42 1.26 c 8.2 14.2 27.4 21.6 45.8 15.4 c 20.2 -6.8 29.4 -24.2 27.2 -40.1 c -0.27 -1.9 -2.03 -3.22 -3.92 -2.95 c -0.45 0.06 -0.88 0.22 -1.28 0.45 C 88.36 68.22 77.75 72.83 66.8 76.5 Z"
        assertTrue(generatedXml.contains(expectedClipPathData), "Generated XML should contain the clip-path data")
    }

    @Test
    fun `round trip test for ic_clip_path xml`() {
        val generatedXml = roundTripGenerateXml("imagevector/xml/ic_clip_path.xml")

        validateVectorAttributes(xml = generatedXml, size = 24, viewportSize = 24f)

        val groupCount = generatedXml.split("<group").lastIndex
        assertEquals(1, groupCount, "Should contain exactly 1 group")

        val clipPathCount = generatedXml.split("<clip-path").lastIndex
        assertEquals(1, clipPathCount, "Should contain exactly 1 clip-path element")

        val pathCount = generatedXml.split("<path").lastIndex
        assertEquals(1, pathCount, "Should contain exactly 1 path element")

        // Verify clip-path data is preserved (circular clip region)
        assertTrue(generatedXml.contains("M 12 2 C 6.48 2 2 6.48 2 12 s 4.48 10 10 10 s 10 -4.48 10 -10 S 17.52 2 12 2 Z"))

        // Verify the path inside the clipped group
        assertTrue(generatedXml.contains("android:fillColor=\"#FFFF0000\""))
        assertTrue(generatedXml.contains("M 0 0 h 24 v 24 h -24 Z"))
    }

    @Test
    fun `parse ImageVector with default values should exclude them from XML output`() {
        val imageVector = IrImageVector(
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
            autoMirror = false,
            nodes = listOf(
                IrVectorNode.IrPath(
                    pathFillType = IrPathFillType.NonZero,
                    fill = IrFill.Color(IrColor(0xffffffff)),
                    fillAlpha = 1f,
                    strokeAlpha = 1f,
                    paths = emptyList(),
                ),
            ),
        )

        val result = IrToXmlGenerator.generate(imageVector)

        with(result) {
            // Default values should not be present in the XML
            assertFalse(contains("android:autoMirrored=\"false\""), "Default autoMirrored=false should not be in XML")
            assertFalse(contains("android:fillType=\"nonZero\""), "Default fillType=nonZero should not be in XML")
            assertFalse(contains("android:fillAlpha=\"1.0\""), "Default fillAlpha=1.0 should not be in XML")
            assertFalse(contains("android:strokeAlpha=\"1.0\""), "Default strokeAlpha=1.0 should not be in XML")

            // Non-default values should be present
            assertTrue(contains("android:fillColor=\"#FFFFFFFF\""), "Non-default fillColor should be in XML")
            assertTrue(contains("android:pathData=\"\""), "pathData should always be present")
        }
    }

    private fun roundTripGenerateXml(resourcePath: String): String {
        val originalXml = getResourceText(resourcePath)
        val imageVector = XmlToImageVectorParser.parse(originalXml)
        return IrToXmlGenerator.generate(imageVector)
    }

    private fun validateVectorAttributes(xml: String, size: Int, viewportSize: Float, autoMirrored: Boolean = false) {
        with(xml) {
            assertTrue(contains("android:width=\"${size}dp\""))
            assertTrue(contains("android:height=\"${size}dp\""))
            assertTrue(contains("android:viewportWidth=\"$viewportSize\""))
            assertTrue(contains("android:viewportHeight=\"$viewportSize\""))
            if (autoMirrored) {
                assertTrue(contains("android:autoMirrored=\"true\""))
            }
        }
    }

    private fun imageVector(nodes: List<IrVectorNode> = emptyList()): IrImageVector = IrImageVector(
        defaultWidth = 24f,
        defaultHeight = 24f,
        viewportWidth = 24f,
        viewportHeight = 24f,
        nodes = nodes,
    )
}
