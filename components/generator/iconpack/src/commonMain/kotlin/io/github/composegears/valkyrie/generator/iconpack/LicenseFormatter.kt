package io.github.composegears.valkyrie.generator.iconpack

internal fun String.asBlockComment(): String {
    if (startsWith("/*") && endsWith("*/")) return this
    return "/*\n${prependIndent(" * ")}\n */"
}
