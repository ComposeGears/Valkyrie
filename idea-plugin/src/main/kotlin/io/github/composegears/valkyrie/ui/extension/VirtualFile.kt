package io.github.composegears.valkyrie.ui.extension

import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path
import kotlin.io.path.Path

fun VirtualFile.toPath(): Path = Path(path)
