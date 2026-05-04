package io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class CssGgSvgPathResolverTest {

    @Test
    fun `resolve css gg svg url`() {
        assertThat(resolveCssGgSvgUrl("/icons/svg/add.svg", version = "2.1.4")).isEqualTo(
            "https://cdn.jsdelivr.net/npm/css.gg@2.1.4/icons/svg/add.svg",
        )
    }
}
