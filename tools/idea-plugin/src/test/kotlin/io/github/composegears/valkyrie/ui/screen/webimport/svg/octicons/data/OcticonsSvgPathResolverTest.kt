package io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class OcticonsSvgPathResolverTest {

    @Test
    fun `resolve octicons svg url`() {
        assertThat(resolveOcticonsSvgUrl("alert-16.svg", version = "19.16.0")).isEqualTo(
            "https://cdn.jsdelivr.net/npm/@primer/octicons@19.16.0/build/svg/alert-16.svg",
        )
    }
}
