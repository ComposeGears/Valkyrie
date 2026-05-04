package io.github.composegears.valkyrie.ui.screen.webimport.svg.cssgg.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class CssGgIndexParser(
    private val json: Json = Json,
) {
    fun parse(text: String): List<CssGgIconMetadata> {
        val root = json.parseToJsonElement(text).jsonObject
        return root["files"]?.jsonArray.orEmpty().mapNotNull { fileElement ->
            val path = fileElement.jsonObject["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
            val parts = path.trimStart('/').split('/')
            if (parts.size != 3 || parts[0] != "icons" || parts[1] != "svg" || !parts[2].endsWith(".svg")) {
                return@mapNotNull null
            }

            val name = parts[2].removeSuffix(".svg")
            CssGgIconMetadata(
                name = name,
                path = path,
                tags = name.split('-'),
            )
        }
    }
}
