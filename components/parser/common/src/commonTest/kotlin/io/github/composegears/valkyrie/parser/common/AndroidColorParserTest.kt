package io.github.composegears.valkyrie.parser.common

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
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

    @Test
    fun `test parsing invalid android colors returns null`() {
        with(AndroidColorParser) {
            assertThat(parse("@android:color/non_existent_color")).isNull()
            assertThat(parse("@android:color/")).isNull()
            assertThat(parse("invalid_prefix/red")).isNull()
            assertThat(parse("")).isNull()
        }
    }
}
