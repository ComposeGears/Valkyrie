package io.github.composegears.valkyrie.generator.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class FloatFormatterTest {

    @Test
    fun `formatValue for floats`() {
        assertThat(24f.trimTrailingZero()).isEqualTo("24")
        assertThat(24.0f.trimTrailingZero()).isEqualTo("24")
        assertThat(24.00002f.trimTrailingZero()).isEqualTo("24.00002")

        assertThat(24f.formatFloat()).isEqualTo("24f")
        assertThat(24.0f.formatFloat()).isEqualTo("24f")
        assertThat(24.00002f.formatFloat()).isEqualTo("24.00002f")
    }
}
