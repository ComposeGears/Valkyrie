package io.github.composegears.valkyrie.cli.command

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isLessThan
import assertk.assertions.isNotEmpty
import com.github.ajalt.clikt.testing.test
import kotlin.test.Test

class ChangelogCommandTest {

    @Test
    fun `should print 5 latest releases`() {
        val result = ChangelogCommand().test()

        assertThat(result.output).contains("CLI Changelog")
        assertThat(result.output).contains("Showing 5 latest releases")
        assertThat(result.output).contains("Use \"--show-all\" to see the full changelog")
    }

    @Test
    fun `should print full changelog with show-all option`() {
        val result = ChangelogCommand().test("--show-all")

        assertThat(result.output).contains("CLI Changelog")
        assertThat(result.output).doesNotContain("Showing 5 latest releases")
        // Should contain all releases including the initial one
        assertThat(result.output).contains("Initial release of Valkyrie CLI tool")
    }

    @Test
    fun `limited changelog should be shorter than full changelog`() {
        val limitedResult = ChangelogCommand().test()
        val fullResult = ChangelogCommand().test("--show-all")

        // Limited version should have the "Showing X latest releases" message
        assertThat(limitedResult.output).contains("Showing 5 latest releases")

        // Full version should not have this message
        assertThat(fullResult.output).doesNotContain("Showing 5 latest releases")

        // Limited version should be shorter than full version
        assertThat(limitedResult.output.length).isLessThan(fullResult.output.length)
    }

    @Test
    fun `help should return changelog description`() {
        val result = ChangelogCommand().test("--help")

        assertThat(result.output).isNotEmpty()
        assertThat(result.output).contains("Print CLI changelog")
        assertThat(result.output).contains("--show-all")
    }
}
