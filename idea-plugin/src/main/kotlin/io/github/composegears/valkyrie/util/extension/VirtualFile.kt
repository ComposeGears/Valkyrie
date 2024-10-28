package io.github.composegears.valkyrie.util.extension

import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.parser.svgxml.util.isSvgExtension
import io.github.composegears.valkyrie.parser.svgxml.util.isXmlExtension

inline val VirtualFile.isSvg: Boolean
    get() = extension?.isSvgExtension ?: false

inline val VirtualFile.isXml: Boolean
    get() = extension?.isXmlExtension ?: false
