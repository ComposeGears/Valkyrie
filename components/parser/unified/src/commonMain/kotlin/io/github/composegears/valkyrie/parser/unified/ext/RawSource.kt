package io.github.composegears.valkyrie.parser.unified.ext

import kotlinx.io.RawSource
import kotlinx.io.buffered
import kotlinx.io.readByteArray

internal fun RawSource.readText() = use {
    it.buffered().readByteArray().decodeToString()
}
