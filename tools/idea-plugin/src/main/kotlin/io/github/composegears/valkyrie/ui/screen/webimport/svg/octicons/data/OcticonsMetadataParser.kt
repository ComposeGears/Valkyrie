package io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class OcticonsMetadataParser(
    private val json: Json = Json,
) {
    fun parse(text: String): List<OcticonsIconMetadata> {
        val root = json.parseToJsonElement(text).jsonObject
        return root.flatMap { (name, iconElement) ->
            val iconObject = iconElement.jsonObject
            val keywords = iconObject["keywords"]
                ?.jsonArray
                ?.mapNotNull { it.jsonPrimitive.contentOrNull }
                .orEmpty()
            val tags = (name.split('-') + keywords).distinct()

            iconObject["heights"]
                ?.jsonObject
                ?.entries
                .orEmpty()
                .sortedBy { it.key.toIntOrNull() ?: Int.MAX_VALUE }
                .mapNotNull { (size, heightElement) ->
                    val width = heightElement.jsonObject["width"]?.jsonPrimitive?.content?.toIntOrNull()
                        ?: return@mapNotNull null
                    OcticonsIconMetadata(
                        name = name,
                        size = size,
                        width = width,
                        path = "$name-$size.svg",
                        tags = tags,
                    )
                }
        }
    }
}
