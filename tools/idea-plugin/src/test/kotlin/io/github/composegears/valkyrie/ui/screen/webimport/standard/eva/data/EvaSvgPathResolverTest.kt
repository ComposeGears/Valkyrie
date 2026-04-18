package io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class EvaSvgPathResolverTest {

    @Test
    fun `resolve fill icon svg url`() {
        assertThat(resolveEvaSvgUrl(iconName = "activity")).isEqualTo(
            "https://cdn.jsdelivr.net/npm/eva-icons@latest/fill/svg/activity.svg",
        )
    }

    @Test
    fun `resolve outline icon svg url`() {
        assertThat(resolveEvaSvgUrl(iconName = "activity-outline")).isEqualTo(
            "https://cdn.jsdelivr.net/npm/eva-icons@latest/outline/svg/activity-outline.svg",
        )
    }
}
