package io.github.composegears.valkyrie.processing.generator.imagevector.ext

fun Float.trimTrailingZero(): String {
    val value = this.toString()

    return when {
        value.endsWith(".0") -> value.replace(".0", "")
        else -> value
    }
}

fun Float.formatFloat(): String = "${trimTrailingZero()}f"

fun String.toColorHex(): String = "0x${this.uppercase()}"