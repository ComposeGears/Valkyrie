package io.github.composegears.valkyrie.parser.unified

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.github.composegears.valkyrie.ir.IrVectorNode
import io.github.composegears.valkyrie.parser.unified.model.IconType
import kotlin.test.Test
import kotlin.test.assertEquals

class SvgXmlParserTest {

    @Test
    fun `parse XML string to IrImageVector`() {
        val xmlContent = """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:width="24dp"
                android:height="24dp"
                android:viewportWidth="24"
                android:viewportHeight="24">
                <path android:fillColor="#FF0000" android:pathData="M10,10h4v4h-4z"/>
            </vector>
        """.trimIndent()

        val iconName = "TestIcon"
        val jvmOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Jvm,
            value = xmlContent,
            iconName = iconName,
        )
        val kmpOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Kmp,
            value = xmlContent,
            iconName = iconName,
        )

        assertThat(jvmOutput.iconType).isEqualTo(IconType.XML)
        assertThat(jvmOutput.iconName).isEqualTo(iconName)
        assertThat(jvmOutput.irImageVector.name).isEqualTo("")

        assertThat(kmpOutput.iconType).isEqualTo(IconType.XML)
        assertThat(kmpOutput.iconName).isEqualTo(iconName)
        assertThat(kmpOutput.irImageVector.name).isEqualTo("")

        assertEquals(jvmOutput, kmpOutput)
    }

    @Test
    fun `parse SVG string to IrImageVector`() {
        val svgContent = """
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                <rect x="10" y="10" width="4" height="4" fill="red"/>
            </svg>
        """.trimIndent()

        val iconName = "TestSvgIcon"
        val jvmOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Jvm,
            value = svgContent,
            iconName = iconName,
        )
        val kmpOutput = SvgXmlParser.toIrImageVector(
            parser = ParserType.Kmp,
            value = svgContent,
            iconName = iconName,
        )

        assertThat(jvmOutput.iconType).isEqualTo(IconType.SVG)
        assertThat(jvmOutput.iconName).isEqualTo(iconName)
        assertThat(jvmOutput.irImageVector.name).isEqualTo("")

        assertThat(kmpOutput.iconType).isEqualTo(IconType.SVG)
        assertThat(kmpOutput.iconName).isEqualTo(iconName)
        assertThat(kmpOutput.irImageVector.name).isEqualTo("")

        assertEquals(
            (jvmOutput.irImageVector.nodes[0] as IrVectorNode.IrPath).paths,
            (kmpOutput.irImageVector.nodes[0] as IrVectorNode.IrPath).paths,
        )
    }

    @Test
    fun `unsupported icon type throws exception`() {
        val unsupportedContent = "This is not a valid SVG or XML content."
        val iconName = "InvalidIcon"

        assertFailure {
            SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, value = unsupportedContent, iconName = iconName)
        }.isInstanceOf(IllegalStateException::class)
            .hasMessage("Unsupported icon type")

        assertFailure {
            SvgXmlParser.toIrImageVector(parser = ParserType.Kmp, value = unsupportedContent, iconName = iconName)
        }.isInstanceOf(IllegalStateException::class)
            .hasMessage("Unsupported icon type")
    }
}
