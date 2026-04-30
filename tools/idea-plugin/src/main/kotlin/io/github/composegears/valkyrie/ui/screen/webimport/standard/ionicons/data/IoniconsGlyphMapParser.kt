package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class IoniconsGlyphMapParser(
    private val json: Json = Json,
) {
    fun parse(text: String): Map<String, Int> {
        return json.parseToJsonElement(text)
            .jsonObject
            .mapValues { (_, value) -> value.jsonPrimitive.int }
    }
}
