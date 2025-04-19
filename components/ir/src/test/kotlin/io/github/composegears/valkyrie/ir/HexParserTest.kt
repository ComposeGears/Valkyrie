package io.github.composegears.valkyrie.ir

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

class HexParserTest {

    @Test
    fun `transparent color test`() {
        assertThat(IrColor("#0000ffcc").isTransparent()).isTrue()
        assertThat(IrColor("#3C545454").isTransparent()).isFalse()
    }

    @Test
    fun `parse to color hex`() {
        val colors = listOf(
            ColorHex(expected = "#FFFFFFFF", actual = IrColor("#FFFFFF").toHexColor()),
            ColorHex(expected = "#FF333333", actual = IrColor("#333").toHexColor()),
            ColorHex(expected = "#FF123456", actual = IrColor("#123456").toHexColor()),
            ColorHex(expected = "#12345678", actual = IrColor("#12345678").toHexColor()),
            ColorHex(expected = "#FF3C3C3C", actual = IrColor("#3c3c3c").toHexColor()),
            ColorHex(expected = "#FFB4B4B4", actual = IrColor("#b4b4b4").toHexColor()),
            ColorHex(expected = "#FF787878", actual = IrColor("#787878").toHexColor()),
            ColorHex(expected = "#FFEEEEEE", actual = IrColor("#EEE").toHexColor()),
            ColorHex(expected = "#FFFFCC99", actual = IrColor("#fc9").toHexColor()),
            ColorHex(expected = "#FFFF00FF", actual = IrColor("#f0f").toHexColor()),
            ColorHex(expected = "#FFBB5588", actual = IrColor("#b58").toHexColor()),
            ColorHex(expected = "#FFFF6347", actual = IrColor("#ffff6347").toHexColor()),
            ColorHex(expected = "#FF3E4E5E", actual = IrColor("#3E4E5E").toHexColor()),
            ColorHex(expected = "#FFFFEEEE", actual = IrColor("#ffeeee").toHexColor()),
            ColorHex(expected = "#FFFFFFFF", actual = IrColor("FFFFFF").toHexColor()),
            ColorHex(expected = "#FF000000", actual = IrColor("#FF000000").toHexColor()),
            ColorHex(expected = "#FF000000", actual = IrColor("#FF000000").toHexColor()),
            ColorHex(expected = "#FF000000", actual = IrColor("#000000").toHexColor()),

            ColorHex(expected = "#FF232F34", actual = IrColor("0xFF232F34").toHexColor()),
            ColorHex(expected = "#FF22FF33", actual = IrColor("0x2F3").toHexColor()),

            // default color
            ColorHex(expected = "#FF000000", actual = IrColor("#FFFFF").toHexColor()),
            ColorHex(expected = "#FF000000", actual = IrColor("#XYZ").toHexColor()),
            ColorHex(expected = "#FF000000", actual = IrColor("").toHexColor()),
            ColorHex(expected = "#FF000000", actual = IrColor("#GHIJKL").toHexColor()),
        )

        colors.forEach {
            assertThat(it.actual).isEqualTo(it.expected)
        }
    }

    @Test
    fun `parse to color names`() {
        listOf(
            IrColor("000000") to "Black",
            IrColor("444444") to "DarkGray",
            IrColor("888888") to "Gray",
            IrColor("CCCCCC") to "LightGray",
            IrColor("FFFFFF") to "White",
            IrColor("FF0000") to "Red",
            IrColor("00FF00") to "Green",
            IrColor("0000FF") to "Blue",
            IrColor("FFFF00") to "Yellow",
            IrColor("00FFFF") to "Cyan",
            IrColor("FF00FF") to "Magenta",
            IrColor("00000000") to "Transparent",
        ).forEach { (color, name) ->
            assertThat(color.toName()).isEqualTo(name)
        }
    }
}

private data class ColorHex(
    val expected: String,
    val actual: String,
)
