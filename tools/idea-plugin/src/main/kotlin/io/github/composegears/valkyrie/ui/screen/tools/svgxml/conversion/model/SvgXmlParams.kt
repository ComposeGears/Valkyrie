package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model

import java.nio.file.Path

sealed interface SvgXmlParams {
    data class PathSource(val path: Path) : SvgXmlParams
    data class TextSource(val svg: String) : SvgXmlParams
}
