package io.github.composegears.valkyrie.generator.iconpack

internal fun String.asBlockComment(): String {
    if (startsWith("/*")) return this
    val body = lines().joinToString("\n") { " * $it" }
    return "/*\n$body\n */"
}
