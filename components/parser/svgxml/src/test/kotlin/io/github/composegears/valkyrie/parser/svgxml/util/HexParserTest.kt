package io.github.composegears.valkyrie.parser.svgxml.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class HexParserTest {

    @Test
    fun `parse string to color hex`() {
        val colors = listOf(
            ColorHex(expected = "FFFFFFFF", actual = "#FFFFFF".toHexColor()),
            ColorHex(expected = "FF333333", actual = "#333".toHexColor()),
            ColorHex(expected = "FF123456", actual = "#123456".toHexColor()),
            ColorHex(expected = "12345678", actual = "#12345678".toHexColor()),
            ColorHex(expected = "FF3c3c3c", actual = "#3c3c3c".toHexColor()),
            ColorHex(expected = "FFb4b4b4", actual = "#b4b4b4".toHexColor()),
            ColorHex(expected = "FF787878", actual = "#787878".toHexColor()),
            ColorHex(expected = "FFEEEEEE", actual = "#EEE".toHexColor()),
            ColorHex(expected = "FFffcc99", actual = "#fc9".toHexColor()),
            ColorHex(expected = "FFff00ff", actual = "#f0f".toHexColor()),
            ColorHex(expected = "FFbb5588", actual = "#b58".toHexColor()),
            ColorHex(expected = "ffff6347", actual = "#ffff6347".toHexColor()),
            ColorHex(expected = "FF3E4E5E", actual = "#3E4E5E".toHexColor()),
            ColorHex(expected = "FFffeeee", actual = "#ffeeee".toHexColor()),
            ColorHex(expected = "FFFFFFFF", actual = "FFFFFF".toHexColor()),
            ColorHex(expected = DEFAULT_COLOR, actual = "#FFFFF".toHexColor()),
            ColorHex(expected = DEFAULT_COLOR, actual = "#XYZ".toHexColor()),
            ColorHex(expected = DEFAULT_COLOR, actual = "".toHexColor()),
            ColorHex(expected = DEFAULT_COLOR, actual = "#GHIJKL".toHexColor()),
        )

        colors.forEach {
            assertThat(it.expected).isEqualTo(it.actual)
        }
    }
}

private data class ColorHex(
    val expected: String,
    val actual: String,
)
