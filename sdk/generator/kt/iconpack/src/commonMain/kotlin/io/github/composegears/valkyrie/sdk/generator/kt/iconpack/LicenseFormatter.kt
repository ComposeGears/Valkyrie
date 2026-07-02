package io.github.composegears.valkyrie.sdk.generator.kt.iconpack

internal fun String.asBlockComment(): String {
    if (startsWith("/*") && endsWith("*/")) return this
    return "/*\n${prependIndent(" * ")}\n */"
}
