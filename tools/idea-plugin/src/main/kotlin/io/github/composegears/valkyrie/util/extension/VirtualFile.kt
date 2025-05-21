package io.github.composegears.valkyrie.util.extension

import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.parser.unified.ext.isSvgExtension
import io.github.composegears.valkyrie.parser.unified.ext.isXmlExtension

inline val VirtualFile.isSvg: Boolean
    get() = extension?.isSvgExtension ?: false

inline val VirtualFile.isXml: Boolean
    get() = extension?.isXmlExtension ?: false
