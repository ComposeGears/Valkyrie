package io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class FeatherSvgPathResolverTest {

    @Test
    fun `resolve feather icon svg url`() {
        assertThat(resolveFeatherSvgUrl("activity")).isEqualTo(
            "https://cdn.jsdelivr.net/npm/feather-icons@latest/dist/icons/activity.svg",
        )
    }
}
