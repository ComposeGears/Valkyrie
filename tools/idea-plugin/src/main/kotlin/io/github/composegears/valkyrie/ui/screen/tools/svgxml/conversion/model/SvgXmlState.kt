package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model

import androidx.compose.runtime.Stable

@Stable
sealed interface SvgXmlState {

    data class Content(
        val fileName: String,
        val xmlCode: XmlCode,
    ) : SvgXmlState

    data class Error(
        val message: String,
        val stacktrace: String? = null,
    ) : SvgXmlState

    data object Loading : SvgXmlState
}

@Stable
data class XmlCode(val value: String)
