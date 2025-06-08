package io.github.composegears.valkyrie.resource.loader

import org.w3c.xhr.XMLHttpRequest

actual object ResourceLoader {

    actual fun getResourceText(name: String): String {
        val request = request(name)

        return when (request.status) {
            in 200..299 -> request.responseText
            else -> error("Resource $name not found")
        }
    }

    private fun request(path: String) = XMLHttpRequest().apply {
        open("GET", path, false)
        send()
    }
}
