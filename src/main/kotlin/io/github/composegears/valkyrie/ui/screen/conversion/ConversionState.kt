package io.github.composegears.valkyrie.ui.screen.conversion

import io.github.composegears.valkyrie.parser.Config

data class ConversionState(
    val initialDirectory: String = "",
    val config: Config? = null
)
