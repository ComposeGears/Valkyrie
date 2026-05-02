package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class HeroiconsIndexParser(
    private val json: Json = Json,
) {
    fun parse(text: String): List<HeroiconsIconMetadata> {
        val root = json.parseToJsonElement(text).jsonObject
        return root["files"]?.jsonArray.orEmpty().mapNotNull { fileElement ->
            val path = fileElement.jsonObject["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
            val parts = path.trimStart('/').split('/')
            if (parts.size != 3 || !parts[2].endsWith(".svg")) return@mapNotNull null

            val size = parts[0]
            val style = parts[1]
            val name = parts[2].removeSuffix(".svg")

            HeroiconsIconMetadata(
                name = name,
                styleId = "$size-$style",
                path = path,
                tags = name.split('-'),
            )
        }
    }
}
