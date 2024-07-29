package io.github.composegears.valkyrie.generator.imagevector.common

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.toPath

object ResourceUtils {

    fun getResourceText(name: String): String = Files.readString(getResourcePath(name)).uniteLine()

    fun getResourcePath(name: String): Path {
        val resource = ResourceUtils::class.java.classLoader.getResource(name) ?: error("Resource $name not found")
        return resource.toURI().toPath()
    }

    private fun String.uniteLine(): String = replace("\r\n", "\n").replace("\r", "\n")
}
