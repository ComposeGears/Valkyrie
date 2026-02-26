package io.github.composegears.valkyrie.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.mordant.markdown.Markdown
import io.github.composegears.valkyrie.cli.ext.flagOption

internal class ChangelogCommand : CliktCommand(name = "changelog") {

    private val showAll by flagOption(
        "--show-all",
        help = "Show full release history",
    )

    override fun run() {
        val changelog = loadResourceText("CHANGELOG.md")
        val filteredChangelog = changelog.replace("## Unreleased\n", "")

        val markdownContent = when {
            showAll -> filteredChangelog
            else -> getLatestReleases(filteredChangelog)
        }
        echo(Markdown(markdown = markdownContent))
    }

    override fun help(context: Context): String = "Print CLI changelog"

    private fun getLatestReleases(changelog: String, count: Int = 5): String {
        val lines = changelog.lines()
        val releaseIndices = lines.indices.filter { index ->
            lines[index].trimStart().startsWith("## ")
        }

        if (releaseIndices.size <= count) {
            return changelog
        }

        val endIndex = releaseIndices[count]
        val limitedLines = lines.subList(0, endIndex)
        val result = limitedLines.joinToString("\n")

        return "$result\n\n---\n\n*Showing $count latest releases. Use \"--show-all\" to see the full changelog.*"
    }
}

private fun Any.loadResourceText(name: String): String {
    val classLoader = this::class.java.classLoader
    val inputStream = classLoader.getResourceAsStream(name) ?: error("Resource '$name' not found")
    return inputStream.bufferedReader().use { it.readText() }
}
