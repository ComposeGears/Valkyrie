package io.github.composegears.valkyrie.generator.util

import io.github.composegears.valkyrie.generator.ext.trimTrailingZero
import org.junit.Test
import kotlin.test.assertEquals

class FormatterTest {

    @Test
    fun `formatValue for floats`() {
        assertEquals(24f.trimTrailingZero(), "24")
        assertEquals(24.0f.trimTrailingZero(), "24")
        assertEquals(24.00002f.trimTrailingZero(), "24.00002")
    }
}