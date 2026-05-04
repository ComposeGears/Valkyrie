package io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class SimpleIconsMetadataParser(
    private val json: Json = Json,
) {
    fun parse(text: String): List<SimpleIconMetadata> {
        return json.parseToJsonElement(text).jsonArray.mapNotNull { iconElement ->
            val iconObject = iconElement.jsonObject
            val title = iconObject["title"]?.jsonPrimitive?.contentOrNull ?: return@mapNotNull null
            val slug = iconObject["slug"]?.jsonPrimitive?.contentOrNull ?: return@mapNotNull null
            val codepoint = iconObject["code"]
                ?.jsonPrimitive
                ?.contentOrNull
                ?.toIntOrNull(radix = 16)
                ?: return@mapNotNull null
            val hex = iconObject["hex"]?.jsonPrimitive?.contentOrNull.orEmpty()
            val aliases = iconObject["aliases"]?.extractStrings().orEmpty()

            SimpleIconMetadata(
                title = title,
                slug = slug,
                codepoint = codepoint,
                hex = hex,
                aliases = aliases,
            )
        }
    }
}

private fun JsonElement.extractStrings(): List<String> {
    return when (this) {
        is JsonPrimitive -> listOfNotNull(contentOrNull)
        is JsonArray -> flatMap { it.extractStrings() }
        is JsonObject -> values.flatMap { it.extractStrings() }
    }
}
