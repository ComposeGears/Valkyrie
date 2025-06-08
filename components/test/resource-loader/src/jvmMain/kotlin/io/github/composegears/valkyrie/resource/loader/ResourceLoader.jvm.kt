package io.github.composegears.valkyrie.resource.loader

import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.toPath

actual object ResourceLoader {
    actual fun getResourceText(name: String): String = getResourcePath(name).readText().replace(System.lineSeparator(), "\n")

    fun getResourcePath(name: String): Path {
        val resource = ResourceLoader::class.java.classLoader.getResource(name) ?: error("Resource $name not found")
        return resource.toURI().toPath()
    }
}
