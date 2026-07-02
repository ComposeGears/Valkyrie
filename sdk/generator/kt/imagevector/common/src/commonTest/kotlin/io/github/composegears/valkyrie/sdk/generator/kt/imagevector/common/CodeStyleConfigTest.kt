package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CodeStyleConfigTest {

    @Test
    fun `accepts positive indent size`() {
        val config = CodeStyleConfig(indentSize = 2)

        assertEquals(2, config.indentSize)
    }

    @Test
    fun `rejects zero indent size`() {
        val error = assertFailsWith<IllegalArgumentException> {
            CodeStyleConfig(indentSize = 0)
        }

        assertEquals("indentSize must be greater than 0", error.message)
    }

    @Test
    fun `rejects negative indent size`() {
        val error = assertFailsWith<IllegalArgumentException> {
            CodeStyleConfig(indentSize = -1)
        }

        assertEquals("indentSize must be greater than 0", error.message)
    }
}
