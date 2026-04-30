package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class IoniconsSvgPathResolverTest {

    @Test
    fun `resolve ionicons svg url`() {
        assertThat(resolveIoniconsSvgUrl("accessibility-outline")).isEqualTo(
            "https://cdn.jsdelivr.net/npm/ionicons@latest/dist/svg/accessibility-outline.svg",
        )
    }
}
