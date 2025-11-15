package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model

import java.nio.file.Path

sealed interface ImageVectorToXmlParams {
    data class PathSource(val path: Path) : ImageVectorToXmlParams
    data class TextSource(val kotlinCode: String) : ImageVectorToXmlParams
}
