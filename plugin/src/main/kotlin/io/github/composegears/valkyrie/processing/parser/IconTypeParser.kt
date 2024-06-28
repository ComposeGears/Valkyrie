package io.github.composegears.valkyrie.processing.parser

object IconTypeParser {

    fun getIconType(fileExtension: String): IconType? = when {
        IconType.SVG.extension.equals(fileExtension, ignoreCase = true) -> IconType.SVG
        IconType.XML.extension.equals(fileExtension, ignoreCase = true) -> IconType.XML
        else -> null
    }
}

enum class IconType(val extension: String) {
    SVG("svg"),
    XML("xml"),
}