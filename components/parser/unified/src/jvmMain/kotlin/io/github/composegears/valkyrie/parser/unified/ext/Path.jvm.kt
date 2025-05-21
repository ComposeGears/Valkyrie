package io.github.composegears.valkyrie.parser.unified.ext

import java.nio.file.Path as NioPath
import java.nio.file.Paths
import kotlinx.io.files.Path

fun Path.toJvmPath(): NioPath = Paths.get(this.toString())

fun NioPath.toIOPath(): Path = Path(this.toString())
