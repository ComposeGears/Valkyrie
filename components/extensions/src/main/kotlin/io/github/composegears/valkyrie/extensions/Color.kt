package io.github.composegears.valkyrie.extensions

fun String.toColorInt() = this
    .trimStart('#')
    .padStart(8, 'F')
    .toLong(16)
    .toInt()
