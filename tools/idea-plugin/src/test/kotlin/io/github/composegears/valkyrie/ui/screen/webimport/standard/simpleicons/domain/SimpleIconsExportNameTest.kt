package io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class SimpleIconsExportNameTest {

    @Test
    fun `prefix export names that start with a digit`() {
        assertThat("1password".toExportName()).isEqualTo("icon-1password")
    }

    @Test
    fun `preserve export names that start with a letter`() {
        assertThat("simpleicons".toExportName()).isEqualTo("simpleicons")
    }
}
