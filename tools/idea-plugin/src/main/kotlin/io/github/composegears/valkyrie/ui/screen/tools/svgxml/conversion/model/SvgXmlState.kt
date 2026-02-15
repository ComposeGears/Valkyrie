package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model

import androidx.compose.runtime.Stable
import java.nio.file.Path

@Stable
sealed interface SvgXmlState {

    data class Content(
        val svgSource: SvgSource,
        val xmlContent: XmlContent,
    ) : SvgXmlState

    data class Error(
        val message: String,
        val stacktrace: String? = null,
    ) : SvgXmlState

    data object Loading : SvgXmlState
}

@Stable
sealed interface SvgSource {
    data class FileBasedIcon(val path: Path) : SvgSource
    data class TextBasedIcon(val svg: String) : SvgSource
}

@Stable
data class XmlContent(val xmlCode: String)
