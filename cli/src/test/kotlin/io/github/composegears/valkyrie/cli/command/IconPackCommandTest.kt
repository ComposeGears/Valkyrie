package io.github.composegears.valkyrie.cli.command

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isNotEmpty
import com.github.ajalt.clikt.testing.test
import kotlin.test.Test

class IconPackCommandTest {

    @Test
    fun `help formatter should include required option marker and default values`() {
        val helpMessage = IconPackCommand().test("--help").output

        assertThat(helpMessage).isNotEmpty()
        assertThat(helpMessage).contains("* --output-path")
        assertThat(helpMessage).contains("(default: 4)")
    }
}
