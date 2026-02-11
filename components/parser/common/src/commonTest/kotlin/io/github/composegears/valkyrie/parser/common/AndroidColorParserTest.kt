package io.github.composegears.valkyrie.parser.common

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.ir.core.IrColor
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
