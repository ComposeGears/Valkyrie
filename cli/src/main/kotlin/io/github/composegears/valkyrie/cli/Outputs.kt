package io.github.composegears.valkyrie.cli

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import kotlin.system.exitProcess

internal fun outputInfo(message: Any) {
    output(message.toString(), TextColors.green)
}

internal fun outputWarn(message: Any) {
    output("Warning: $message", TextColors.yellow)
}

internal fun outputError(message: Any): Nothing {
    output("Error: $message", TextColors.red, true)
    exitProcess(1)
}

private fun output(message: String, color: TextColors, stderr: Boolean = false) {
    terminal.println(message = color(message), stderr = stderr)
}

private val terminal = Terminal()
