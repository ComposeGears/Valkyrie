package io.github.composegears.valkyrie.ui.screen.webimport.lucide.data

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

class LucideRepository(
    private val httpClient: HttpClient,
    private val json: Json,
) {
    companion object {
        private const val UNPKG_BASE = "https://unpkg.com/lucide-static@latest"
        private const val FONT_URL = "$UNPKG_BASE/font/lucide.woff2"
        private const val CSS_URL = "https://cdn.jsdelivr.net/npm/lucide-static@latest/font/lucide.css"
    }

    private val fontMutex = Mutex()
    private val codepointMutex = Mutex()
    private var fontBytesCache: ByteArray? = null
    private var codepointsCache: Map<String, Int>? = null

    suspend fun loadIconList(): List<Pair<String, LucideIconMetadata>> = withContext(Dispatchers.IO) {
        val response = httpClient.get("$UNPKG_BASE/tags.json")
        val tagsJson = json.parseToJsonElement(response.bodyAsText()) as JsonObject

        tagsJson.entries.map { (iconName, tagsArray) ->
            val tags = tagsArray.jsonArray.map { it.jsonPrimitive.content }
            iconName to LucideIconMetadata(
                tags = tags,
                categories = emptyList(), // Categories will be inferred from tags
            )
        }
    }

    suspend fun loadFontBytes(): ByteArray = withContext(Dispatchers.IO) {
        fontMutex.withLock {
            fontBytesCache ?: run {
                val bytes = httpClient.get(FONT_URL).bodyAsChannel().toByteArray()
                fontBytesCache = bytes
                bytes
            }
        }
    }

    suspend fun loadCodepoints(): Map<String, Int> = withContext(Dispatchers.IO) {
        codepointMutex.withLock {
            codepointsCache ?: run {
                val cssText = httpClient.get(CSS_URL).bodyAsText()
                val codepoints = parseCodepoints(cssText)
                codepointsCache = codepoints
                codepoints
            }
        }
    }

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        httpClient.get("$UNPKG_BASE/icons/$iconName.svg").bodyAsText()
    }

    private fun parseCodepoints(cssText: String): Map<String, Int> {
        val pattern = Regex("""\.icon-([a-z0-9-]+)::?before\s*\{\s*content:\s*["']\\([a-fA-F0-9]+)["'];\s*}""")

        return pattern.findAll(cssText).mapNotNull { match ->
            val name = match.groupValues[1]
            val codepointHex = match.groupValues[2]
            val codepoint = codepointHex.toIntOrNull(16) ?: return@mapNotNull null
            name to codepoint
        }.toMap()
    }
}
