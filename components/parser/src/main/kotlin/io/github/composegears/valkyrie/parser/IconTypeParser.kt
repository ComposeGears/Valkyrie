package io.github.composegears.valkyrie.parser

internal object IconTypeParser {

    fun getIconType(fileExtension: String): IconType? = when {
        IconType.SVG.extension.equals(fileExtension, ignoreCase = true) -> IconType.SVG
        IconType.XML.extension.equals(fileExtension, ignoreCase = true) -> IconType.XML
        else -> null
    }
}

internal enum class IconType(val extension: String) {
    SVG("svg"),
    XML("xml"),
}
