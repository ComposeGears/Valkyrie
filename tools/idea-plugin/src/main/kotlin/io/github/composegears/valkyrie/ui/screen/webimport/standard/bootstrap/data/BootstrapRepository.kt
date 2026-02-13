package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data

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
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class BootstrapRepository(
    private val httpClient: HttpClient,
    private val json: Json,
) {
    companion object {
        private const val UNPKG_BASE = "https://unpkg.com/bootstrap-icons@latest"
        private const val FONT_URL = "$UNPKG_BASE/font/fonts/bootstrap-icons.woff2"
        private const val JSON_URL = "$UNPKG_BASE/font/bootstrap-icons.json"
        private const val ICONS_BASE_URL = "$UNPKG_BASE/icons"
    }

    private val fontMutex = Mutex()
    private val codepointMutex = Mutex()
    private var fontBytesCache: ByteArray? = null
    private var codepointsCache: Map<String, Int>? = null

    suspend fun loadIconList(): Map<String, Int> = loadCodepoints()

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
                val jsonText = httpClient.get(JSON_URL).bodyAsText()
                val codepoints = parseCodepoints(jsonText)
                codepointsCache = codepoints
                codepoints
            }
        }
    }

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        httpClient.get("$ICONS_BASE_URL/$iconName.svg").bodyAsText()
    }

    private fun parseCodepoints(jsonText: String): Map<String, Int> {
        val jsonObject = json.parseToJsonElement(jsonText).jsonObject
        return jsonObject.entries.mapNotNull { (name, value) ->
            val codepoint = value.jsonPrimitive.content.toIntOrNull() ?: return@mapNotNull null
            name to codepoint
        }.toMap()
    }
}
