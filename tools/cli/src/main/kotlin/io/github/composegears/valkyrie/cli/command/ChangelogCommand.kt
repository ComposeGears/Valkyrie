package io.github.composegears.valkyrie.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.mordant.markdown.Markdown

internal class ChangelogCommand : CliktCommand(name = "changelog") {

    override fun run() {
        echo(Markdown(loadResourceText("CHANGELOG.md"), showHtml = true))
    }

    override fun help(context: Context): String = "Print CLI changelog"
}

private fun Any.loadResourceText(name: String): String {
    val classLoader = this::class.java.classLoader
    val inputStream = classLoader.getResourceAsStream(name) ?: error("Resource '$name' not found")
    return inputStream.bufferedReader().use { it.readText() }
}
