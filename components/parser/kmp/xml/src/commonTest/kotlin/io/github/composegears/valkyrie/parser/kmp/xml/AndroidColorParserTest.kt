package io.github.composegears.valkyrie.parser.kmp.xml

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ir.IrColor
import kotlin.test.Test

class AndroidColorParserTest {

    @Test
    fun `test parsing xml android colors`() {
        val colorMap = AndroidColorParser.androidColorsMap

        colorMap.forEach { (colorName, hexValue) ->
            val androidColorName = "@android:color/$colorName"

            assertThat(actual = AndroidColorParser.parse(androidColorName)).isEqualTo(IrColor(hexValue))
        }
    }
}
