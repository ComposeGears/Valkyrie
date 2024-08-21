package io.github.composegears.valkyrie.extensions

import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.toPath

object ResourceUtils {

  fun getResourceText(name: String): String = getResourcePath(name).readText().replace(System.lineSeparator(), "\n")

  fun getResourcePath(name: String): Path {
    val resource = ResourceUtils::class.java.classLoader.getResource(name) ?: error("Resource $name not found")
    return resource.toURI().toPath()
  }
}
