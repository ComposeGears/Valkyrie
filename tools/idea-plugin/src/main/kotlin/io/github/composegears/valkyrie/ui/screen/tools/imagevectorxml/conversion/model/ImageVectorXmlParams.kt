package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model

import java.nio.file.Path

sealed interface ImageVectorXmlParams {
    data class PathSource(val path: Path) : ImageVectorXmlParams
    data class TextSource(val kotlinCode: String) : ImageVectorXmlParams
}
