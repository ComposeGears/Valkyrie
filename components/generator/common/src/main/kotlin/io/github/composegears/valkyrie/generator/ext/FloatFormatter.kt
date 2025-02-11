package io.github.composegears.valkyrie.generator.ext

fun Float.trimTrailingZero(): String {
    return toString().removeSuffix(".0")
}

fun Float.formatFloat(): String = "${trimTrailingZero()}f"
