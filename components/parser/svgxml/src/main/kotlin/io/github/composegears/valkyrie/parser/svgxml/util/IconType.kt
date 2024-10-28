package io.github.composegears.valkyrie.parser.svgxml.util

import java.nio.file.Path
import kotlin.io.path.extension

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

inline val Path.isSvg: Boolean
    get() = extension.isSvgExtension

inline val Path.isXml: Boolean
    get() = extension.isXmlExtension

inline val String.isSvgExtension: Boolean
    get() = equals(other = IconType.SVG.extension, ignoreCase = true)

inline val String.isXmlExtension: Boolean
    get() = equals(other = IconType.XML.extension, ignoreCase = true)

private val String.isSvg: Boolean
    get() = contains("<svg")

private val String.isXml: Boolean
    get() = contains("<vector")
