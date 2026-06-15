package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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
    fun `from throws for invalid key`() {
        val error = assertFailsWith<IllegalStateException> {
            OutputFormat.from("invalid_key")
        }

        assertEquals(
            "Unsupported outputFormat 'invalid_key'. Supported values: 'backing_property', 'lazy_property'.",
            error.message,
        )
    }

    @Test
    fun `fromOrNull returns null for unsupported keys`() {
        assertNull(OutputFormat.fromOrNull("invalid_key"))
        assertNull(OutputFormat.fromOrNull(null))
    }
}
