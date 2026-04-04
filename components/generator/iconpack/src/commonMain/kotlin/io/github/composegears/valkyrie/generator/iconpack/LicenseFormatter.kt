package io.github.composegears.valkyrie.generator.iconpack

internal fun String.asBlockComment(): String {
    val trimmed = trim()
    if (trimmed.startsWith("/*") && trimmed.endsWith("*/")) return this
    val body = lines().joinToString("\n") { " * $it" }
    return "/*\n$body\n */"
}