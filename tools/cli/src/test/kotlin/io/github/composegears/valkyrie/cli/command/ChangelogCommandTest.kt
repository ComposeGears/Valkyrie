package io.github.composegears.valkyrie.cli.command

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isNotEmpty
import com.github.ajalt.clikt.testing.test
import kotlin.test.Test

class ChangelogCommandTest {

    @Test
    fun `run should print CHANGELOG content`() {
        val result = ChangelogCommand().test()

        assertThat(result.output).contains("CLI Changelog")
    }

    @Test
    fun `help should return changelog description`() {
        val result = ChangelogCommand().test("--help")

        assertThat(result.output).isNotEmpty()
        assertThat(result.output).contains("Print CLI changelog")
    }
}
