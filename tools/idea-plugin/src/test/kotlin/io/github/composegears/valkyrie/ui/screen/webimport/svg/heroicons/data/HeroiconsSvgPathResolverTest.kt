package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class HeroiconsSvgPathResolverTest {

    @Test
    fun `resolve heroicons svg url`() {
        assertThat(resolveHeroiconsSvgUrl("/24/outline/academic-cap.svg", version = "2.2.0")).isEqualTo(
            "https://cdn.jsdelivr.net/npm/heroicons@2.2.0/24/outline/academic-cap.svg",
        )
    }
}
