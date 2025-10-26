package io.github.composegears.valkyrie.ui.screen.webimport.material.data.config

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class MaterialSymbolsConfigRepository(
    private val httpClient: HttpClient,
    private val json: Json,
) {

    suspend fun load(): MaterialIconsMetadata = withContext(Dispatchers.IO) {
        val response = httpClient.get("https://fonts.google.com/metadata/icons?key=material_symbols&incomplete=true")
        val rawText = response.bodyAsText()

        // Remove the ")]}'" prefix that Google adds for security
        val jsonText = rawText.removePrefix(")]}'\n")
        json.decodeFromString<MaterialIconsMetadata>(jsonText)
    }
}
