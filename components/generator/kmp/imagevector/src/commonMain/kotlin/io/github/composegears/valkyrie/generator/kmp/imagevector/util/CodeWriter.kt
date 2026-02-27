package io.github.composegears.valkyrie.generator.kmp.imagevector.util

internal class CodeWriter(private val indentSize: Int) {
    private val value = StringBuilder()

    fun indent(level: Int): String = " ".repeat(indentSize * level)

    fun append(text: String) {
        value.append(text)
    }

    fun newLine() {
        value.append('\n')
    }

    fun line(text: String = "") {
        value.append(text)
        value.append('\n')
    }

    override fun toString(): String = value.toString().trimEnd('\n') + "\n"
}
