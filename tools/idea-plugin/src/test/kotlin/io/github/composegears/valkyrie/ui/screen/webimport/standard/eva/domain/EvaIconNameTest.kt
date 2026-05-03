package io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.icon.IconStyle
import org.junit.jupiter.api.Test

class EvaIconNameTest {

    @Test
    fun `infer eva style from icon name`() {
        assertThat("activity".toEvaStyle()).isEqualTo(IconStyle(id = "fill", name = "Fill"))
        assertThat("activity-outline".toEvaStyle()).isEqualTo(IconStyle(id = "outline", name = "Outline"))
    }

    @Test
    fun `strip eva style suffix from display name`() {
        assertThat("activity".toEvaDisplayName()).isEqualTo("Activity")
        assertThat("activity-outline".toEvaDisplayName()).isEqualTo("Activity")
        assertThat("bell-off-outline".toEvaDisplayName()).isEqualTo("Bell Off")
    }
}
