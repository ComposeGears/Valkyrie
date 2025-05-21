package io.github.composegears.valkyrie.parser.unified.model

import io.github.composegears.valkyrie.parser.unified.ext.isSvg
import io.github.composegears.valkyrie.parser.unified.ext.isXml
import kotlinx.io.files.Path

enum class IconType(val extension: String) {
    SVG("svg"),
    XML("xml"),
    ;

    companion object {
        fun from(path: Path): IconType? = when {
            path.isSvg -> SVG
            path.isXml -> XML
            else -> null
        }

        fun from(text: String): IconType? = when {
            text.isSvg -> SVG
            text.isXml -> XML
            else -> null
        }
    }
}

private val String.isSvg: Boolean
    get() = contains("<svg")

private val String.isXml: Boolean
    get() = contains("<vector")
