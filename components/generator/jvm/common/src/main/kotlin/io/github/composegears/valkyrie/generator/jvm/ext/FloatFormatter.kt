package io.github.composegears.valkyrie.generator.jvm.ext

fun Float.trimTrailingZero(): String {
    return toString().removeSuffix(".0")
}

fun Float.formatFloat(): String = "${trimTrailingZero()}f"
