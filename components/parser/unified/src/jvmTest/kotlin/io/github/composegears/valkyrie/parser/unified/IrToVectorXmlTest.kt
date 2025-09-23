package io.github.composegears.valkyrie.parser.unified

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.startsWith
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.resource.loader.ResourceLoader.getResourcePath
import kotlin.test.Test
import kotlin.test.assertEquals

class IrToVectorXmlTest {

    @Test
    fun `convert simple path to XML`() {
        val iconPath = getResourcePath("imagevector/xml/ic_only_path.xml").toIOPath()
        val irImageVector = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = irImageVector.toVectorXmlString()

        assertThat(xmlOutput).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        assertThat(xmlOutput).contains("<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"")
        assertThat(xmlOutput).contains("android:width=\"24dp\"")
        assertThat(xmlOutput).contains("android:height=\"24dp\"")
        assertThat(xmlOutput).contains("android:viewportWidth=\"18\"")
        assertThat(xmlOutput).contains("android:viewportHeight=\"18\"")
        assertThat(xmlOutput).contains("<path")
        assertThat(xmlOutput).contains("android:pathData=")
        assertThat(xmlOutput).contains("M") // Move command
        assertThat(xmlOutput).contains("L") // Line command
        assertThat(xmlOutput).contains("6.75") // Coordinate values
        assertThat(xmlOutput).contains("12.127")
        assertThat(xmlOutput).contains("3.623")
        assertThat(xmlOutput.trim()).contains("</vector>")
    }

    @Test
    fun `convert path with fill and stroke to XML`() {
        val iconPath = getResourcePath("imagevector/xml/ic_fill_color_stroke.xml").toIOPath()
        val irImageVector = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = irImageVector.toVectorXmlString()

        assertThat(xmlOutput).contains("android:fillColor=\"#FF232F34\"")
        assertThat(xmlOutput).contains("android:strokeWidth=\"1\"")
    }

    @Test
    fun `convert path with all attributes to XML`() {
        val iconPath = getResourcePath("imagevector/xml/ic_all_path_params.xml").toIOPath()
        val irImageVector = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = irImageVector.toVectorXmlString()

        assertThat(xmlOutput).contains("android:autoMirrored=\"true\"")
        assertThat(xmlOutput).contains("android:name=\"path_name\"")
        assertThat(xmlOutput).contains("android:fillColor=\"#FF232F34\"")
        assertThat(xmlOutput).contains("android:fillAlpha=\"0.5\"")
        assertThat(xmlOutput).contains("android:fillType=\"evenOdd\"")
        assertThat(xmlOutput).contains("android:strokeColor=\"#FF232F34\"")
        assertThat(xmlOutput).contains("android:strokeWidth=\"1\"")
        assertThat(xmlOutput).contains("android:strokeAlpha=\"0.5\"")
        assertThat(xmlOutput).contains("android:strokeLineCap=\"round\"")
        assertThat(xmlOutput).contains("android:strokeLineJoin=\"round\"")
        assertThat(xmlOutput).contains("android:strokeMiterLimit=\"3\"")
    }

    @Test
    fun `convert group with all attributes to XML`() {
        val iconPath = getResourcePath("imagevector/xml/ic_all_group_params.xml").toIOPath()
        val irImageVector = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = irImageVector.toVectorXmlString()

        assertThat(xmlOutput).contains("<group")
        assertThat(xmlOutput).contains("android:name=\"group\"")
        assertThat(xmlOutput).contains("android:pivotX=\"10\"")
        assertThat(xmlOutput).contains("android:pivotY=\"10\"")
        assertThat(xmlOutput).contains("android:translateX=\"6\"")
        assertThat(xmlOutput).contains("android:translateY=\"1\"")
        assertThat(xmlOutput).contains("android:scaleX=\"0.8\"")
        assertThat(xmlOutput).contains("android:scaleY=\"0.8\"")
        assertThat(xmlOutput).contains("android:rotation=\"15\"")
        assertThat(xmlOutput).contains("</group>")

        assertThat(xmlOutput).contains("android:fillAlpha=\"0.3\"")
    }

    @Test
    fun `convert multiple paths to XML`() {
        val iconPath = getResourcePath("imagevector/xml/ic_several_path.xml").toIOPath()
        val irImageVector = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = irImageVector.toVectorXmlString()

        val pathCount = xmlOutput.split("<path").lastIndex
        assertThat(pathCount > 1).isEqualTo(true)
    }

    @Test
    fun `convert transparent fill color to XML`() {
        val iconPath = getResourcePath("imagevector/xml/ic_transparent_fill_color.xml").toIOPath()
        val irImageVector = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = irImageVector.toVectorXmlString()

        assertThat(xmlOutput.contains("android:fillColor")).isEqualTo(false)
    }

    @Test
    fun `roundtrip conversion maintains data integrity`() {
        val iconPath = getResourcePath("imagevector/xml/ic_all_path_params.xml").toIOPath()
        val originalIr = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = originalIr.toVectorXmlString()
        val roundtripIr =
            SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, value = xmlOutput, iconName = "test").irImageVector

        assertEquals(originalIr.defaultWidth, roundtripIr.defaultWidth)
        assertEquals(originalIr.defaultHeight, roundtripIr.defaultHeight)
        assertEquals(originalIr.viewportWidth, roundtripIr.viewportWidth)
        assertEquals(originalIr.viewportHeight, roundtripIr.viewportHeight)
        assertEquals(originalIr.autoMirror, roundtripIr.autoMirror)
        assertEquals(originalIr.nodes.size, roundtripIr.nodes.size)
    }

    @Test
    fun `XML output has proper formatting`() {
        val iconPath = getResourcePath("imagevector/xml/ic_only_path.xml").toIOPath()
        val irImageVector = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = irImageVector.toVectorXmlString()

        val lines = xmlOutput.lines()
        assertThat(lines.first()).isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")

        assertThat(lines.size > 2).isEqualTo(true)
        assertThat(xmlOutput).contains("xmlns:android=")

        assertThat(xmlOutput).contains("\n<vector")
        assertThat(xmlOutput).contains("</vector>")
    }

    @Test
    fun `default values are not written to XML`() {
        val iconPath = getResourcePath("imagevector/xml/ic_only_path.xml").toIOPath()
        val irImageVector = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath).irImageVector

        val xmlOutput = irImageVector.toVectorXmlString()

        with(xmlOutput) {
            assertThat(contains("android:fillAlpha=\"1\"")).isEqualTo(false)
            assertThat(contains("android:strokeAlpha=\"1\"")).isEqualTo(false)
            assertThat(contains("android:scaleX=\"1\"")).isEqualTo(false)
            assertThat(contains("android:scaleY=\"1\"")).isEqualTo(false)
            assertThat(contains("android:pivotX=\"0\"")).isEqualTo(false)
            assertThat(contains("android:pivotY=\"0\"")).isEqualTo(false)
            assertThat(contains("android:rotation=\"0\"")).isEqualTo(false)
        }
    }
}
