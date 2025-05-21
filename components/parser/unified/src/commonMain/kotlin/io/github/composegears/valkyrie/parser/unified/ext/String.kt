package io.github.composegears.valkyrie.parser.unified.ext

import io.github.composegears.valkyrie.parser.unified.model.IconType

fun String.removePrefix(prefix: CharSequence): String {
    if (startsWith(prefix, ignoreCase = true)) {
        return substring(prefix.length)
    }
    return this
}

fun String.capitalized(): String = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase() else it.toString()
}

inline val String.isSvgExtension: Boolean
    get() = equals(other = IconType.SVG.extension, ignoreCase = true)

inline val String.isXmlExtension: Boolean
    get() = equals(other = IconType.XML.extension, ignoreCase = true)
