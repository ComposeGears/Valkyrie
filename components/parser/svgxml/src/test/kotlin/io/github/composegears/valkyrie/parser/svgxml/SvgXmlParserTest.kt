package io.github.composegears.valkyrie.parser.svgxml

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.SVG
import io.github.composegears.valkyrie.parser.svgxml.util.IconType.XML
import org.junit.jupiter.api.Test

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
        val parserOutput = SvgXmlParser.toIrImageVector(value = xmlContent, iconName = iconName)

        assertThat(parserOutput.iconType).isEqualTo(XML)
        assertThat(parserOutput.iconName).isEqualTo(iconName)
        assertThat(parserOutput.irImageVector.name).isEqualTo("")
    }

    @Test
    fun `parse SVG string to IrImageVector`() {
        val svgContent = """
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                <rect x="10" y="10" width="4" height="4" fill="red"/>
            </svg>
        """.trimIndent()

        val iconName = "TestSvgIcon"
        val parserOutput = SvgXmlParser.toIrImageVector(value = svgContent, iconName = iconName)

        assertThat(parserOutput.iconType).isEqualTo(SVG)
        assertThat(parserOutput.iconName).isEqualTo(iconName)
        assertThat(parserOutput.irImageVector.name).isEqualTo("")
    }

    @Test
    fun `unsupported icon type throws exception`() {
        val unsupportedContent = "This is not a valid SVG or XML content."
        val iconName = "InvalidIcon"

        assertFailure {
            SvgXmlParser.toIrImageVector(value = unsupportedContent, iconName = iconName)
        }.isInstanceOf(IllegalStateException::class)
            .hasMessage("Unsupported icon type")
    }
}
