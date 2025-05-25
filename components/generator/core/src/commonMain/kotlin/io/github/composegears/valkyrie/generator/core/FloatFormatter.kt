package io.github.composegears.valkyrie.generator.core

fun Float.trimTrailingZero(): String = toString().removeSuffix(".0")

fun Float.formatFloat(): String = "${trimTrailingZero()}f"
