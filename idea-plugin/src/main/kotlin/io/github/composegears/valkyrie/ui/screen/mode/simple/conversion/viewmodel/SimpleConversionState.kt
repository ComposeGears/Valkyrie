package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.viewmodel

import io.github.composegears.valkyrie.parser.svgxml.util.IconType
import java.nio.file.Path

sealed interface SimpleConversionState {
    data object PickerState : SimpleConversionState

    data class ConversionState(
        val iconSource: IconSource,
        val iconContent: IconContent,
    ) : SimpleConversionState
}

sealed interface IconSource {
    data class FileBasedIcon(val path: Path) : IconSource
    data class StringBasedIcon(val text: String) : IconSource
}

data class IconContent(
    val name: String,
    val code: String,
    val iconType: IconType,
)
