package io.github.composegears.valkyrie.parser.unified.ext

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

val Path.extension: String
    get() = name.substringAfterLast('.', "")

internal fun Path.readText(): String {
    return SystemFileSystem.source(this).readText()
}

inline val Path.isSvg: Boolean
    get() = extension.isSvgExtension

inline val Path.isXml: Boolean
    get() = extension.isXmlExtension
