package io.github.composegears.valkyrie.generator.util

fun Float.trimTrailingZero(): String {
    val value = this.toString()

    return when {
        value.endsWith(".0") -> value.replace(".0", "")
        else -> value
    }
}
