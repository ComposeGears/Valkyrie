package io.github.composegears.valkyrie.ui.extension

import com.intellij.openapi.vfs.VirtualFile
import java.io.File

fun VirtualFile.toFile() = File(path)