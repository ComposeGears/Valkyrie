package io.github.composegears.valkyrie.sdk.generator.kt.util

public fun Float.trimTrailingZero(): String = toString().removeSuffix(".0")

public fun Float.formatFloat(): String = "${trimTrailingZero()}f"
