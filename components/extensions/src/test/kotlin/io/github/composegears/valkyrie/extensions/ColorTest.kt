package io.github.composegears.valkyrie.extensions

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class ColorTest {

    @Test
    fun `string to color int`() {
        assertThat("#FF000000".toColorInt()).isEqualTo(-16777216)
        assertThat("FF000000".toColorInt()).isEqualTo(-16777216)
        assertThat("000000".toColorInt()).isEqualTo(-16777216)
    }
}
