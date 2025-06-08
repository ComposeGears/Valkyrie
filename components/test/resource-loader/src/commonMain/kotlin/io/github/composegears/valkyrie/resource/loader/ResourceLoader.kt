package io.github.composegears.valkyrie.resource.loader

expect object ResourceLoader {

    fun getResourceText(name: String): String
}
