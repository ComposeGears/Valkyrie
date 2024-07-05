package io.github.composegears.valkyrie.generator.ext

import org.junit.Test
import kotlin.test.assertEquals

class FormatterTest {

    @Test
    fun `formatValue for floats`() {
        assertEquals(24f.trimTrailingZero(), "24")
        assertEquals(24.0f.trimTrailingZero(), "24")
        assertEquals(24.00002f.trimTrailingZero(), "24.00002")

        assertEquals(24f.formatFloat(), "24f")
        assertEquals(24.0f.formatFloat(), "24f")
        assertEquals(24.00002f.formatFloat(), "24.00002f")

        assertEquals("FFe676ff".toColorHex(), "0xFFE676FF")
    }
}