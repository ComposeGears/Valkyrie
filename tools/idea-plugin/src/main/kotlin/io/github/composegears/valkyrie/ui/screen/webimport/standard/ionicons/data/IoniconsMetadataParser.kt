package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class IoniconsMetadataParser(
    private val json: Json = Json,
) {
    fun parse(text: String): List<IoniconsIconMetadata> {
        val root = json.parseToJsonElement(text).jsonObject
        return root["icons"]?.jsonArray.orEmpty().mapNotNull { iconElement ->
            val icon = iconElement.jsonObject
            val name = icon["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
            val tags = icon["tags"]?.jsonArray.orEmpty().map { it.jsonPrimitive.content }
            IoniconsIconMetadata(name = name, tags = tags)
        }
    }
}
