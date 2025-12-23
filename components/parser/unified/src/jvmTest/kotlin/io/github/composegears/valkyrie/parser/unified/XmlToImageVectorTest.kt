package io.github.composegears.valkyrie.parser.unified

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourcePath
import kotlin.test.assertEquals
import kotlinx.io.files.Path
import org.junit.jupiter.api.Test

class XmlToImageVectorTest {

    @Test
    fun `broken icon path should throw exception`() {
        val brokenIconPath = Path(path = "")

        assertFailure {
            SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = brokenIconPath)
        }.isInstanceOf(IllegalStateException::class)
            .hasMessage(" must be an SVG or XML file.")

        assertFailure {
            SvgXmlParser.toIrImageVector(parser = ParserType.Kmp, path = brokenIconPath)
        }.isInstanceOf(IllegalStateException::class)
            .hasMessage(" must be an SVG or XML file.")
    }

    @Test
    fun `generation without icon pack`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_without_path.xml").toIOPath())
    }

    @Test
    fun `icon only with path`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_only_path.xml").toIOPath())
    }

    @Test
    fun `icon with path and solid color`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_fill_color_stroke.xml").toIOPath())
    }

    @Test
    fun `icon with all path params`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_all_path_params.xml").toIOPath())
    }

    @Test
    fun `icon with all group params`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_all_group_params.xml").toIOPath())
    }

    @Test
    fun `icon with several path`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_several_path.xml").toIOPath())
    }

    @Test
    fun `icon with compose colors enabled`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_compose_color.xml").toIOPath())
    }

    @Test
    fun `icon with compose colors and linear gradient`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_compose_color_linear_gradient.xml").toIOPath())
    }

    @Test
    fun `icon with compose colors and radial gradient`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_compose_color_radial_gradient.xml").toIOPath())
    }

    @Test
    fun `icon with transparent fill color`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_transparent_fill_color.xml").toIOPath())
    }

    @Test
    fun `icon with named arguments`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/icon_with_named_args.xml").toIOPath())
    }

    @Test
    fun `icon with shorthand color`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/icon_with_shorthand_color.xml").toIOPath())
    }

    @Test
    fun `icon with clip path`() {
        unifiedXmlParserTest(iconPath = getResourcePath("imagevector/xml/ic_clip_path.xml").toIOPath())
    }

    private fun unifiedXmlParserTest(iconPath: Path) {
        val jvmOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Jvm, path = iconPath)
        val kmpOutput = SvgXmlParser.toIrImageVector(parser = ParserType.Kmp, path = iconPath)

        assertThat(jvmOutput.iconType).isEqualTo(IconType.XML)
        assertThat(kmpOutput.iconType).isEqualTo(IconType.XML)
        assertEquals(jvmOutput, kmpOutput)
    }
}
