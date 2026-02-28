package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain

import io.github.composegears.valkyrie.parser.unified.ext.capitalized

internal fun String.toDisplayName(): String {
    return split('-')
        .joinToString(separator = " ") { it.capitalized() }
}
