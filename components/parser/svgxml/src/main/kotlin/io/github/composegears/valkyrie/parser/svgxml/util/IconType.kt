package io.github.composegears.valkyrie.parser.svgxml.util

import java.nio.file.Path
import kotlin.io.path.extension

@PublishedApi
internal enum class IconType(val extension: String) {
    SVG("svg"),
    XML("xml"),
    ;

    companion object {
        fun from(extension: String): IconType? = when {
            extension.isSvg -> SVG
            extension.isXml -> XML
            else -> null
        }
    }
}

inline val String?.isSvg: Boolean get() = equals(other = IconType.SVG.extension, ignoreCase = true)
inline val String?.isXml: Boolean get() = equals(other = IconType.XML.extension, ignoreCase = true)

inline val Path.isSvg: Boolean get() = extension.isSvg
inline val Path.isXml: Boolean get() = extension.isXml
