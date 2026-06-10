package io.github.composegears.valkyrie.sdk.generator.kt.util

fun Float.trimTrailingZero(): String = toString().removeSuffix(".0")

fun Float.formatFloat(): String = "${trimTrailingZero()}f"
