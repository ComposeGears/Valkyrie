package io.github.composegears.valkyrie.parser.ktfile.util

internal fun String.toColorInt() = this
    .trimStart('#')
    .padStart(8, 'F')
    .toLong(16)
    .toInt()
