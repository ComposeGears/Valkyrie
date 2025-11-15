package io.github.composegears.valkyrie.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context

internal class ChangelogCommand : CliktCommand(name = "changelog") {

    override fun run() {
        echo(loadResourceText("CHANGELOG.md"))
    }

    override fun help(context: Context): String = "Print CLI changelog"
}

fun Any.loadResourceText(name: String): String {
    val classLoader = this::class.java.classLoader
    val inputStream = classLoader.getResourceAsStream(name) ?: error("Resource '$name' not found")
    return inputStream.bufferedReader().use { it.readText() }
}
