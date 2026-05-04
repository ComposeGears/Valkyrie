package io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class SimpleIconsSvgPathResolverTest {

    @Test
    fun `resolve simple icons svg url`() {
        assertThat(resolveSimpleIconsSvgUrl("github", version = "16.18.1")).isEqualTo(
            "https://cdn.jsdelivr.net/npm/simple-icons@16.18.1/icons/github.svg",
        )
    }
}
