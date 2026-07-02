package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OutputFormatTest {

    @Test
    fun `from returns output format for valid key`() {
        val result = OutputFormat.from("backing_property")
        assertEquals(OutputFormat.BackingProperty, result)

        val resultLazy = OutputFormat.from("lazy_property")
        assertEquals(OutputFormat.LazyProperty, resultLazy)
    }

    @Test
    fun `fromOrNull returns null for unsupported keys`() {
        assertNull(OutputFormat.from("invalid_key"))
        assertNull(OutputFormat.from(null))
    }
}
