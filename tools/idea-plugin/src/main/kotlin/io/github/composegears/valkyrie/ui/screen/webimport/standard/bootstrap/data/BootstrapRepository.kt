package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data

import io.github.composegears.valkyrie.util.font.Woff2Decoder
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

    suspend fun loadFontBytes(): ByteArray = withContext(Dispatchers.IO) {
        fontMutex.withLock {
            fontBytesCache ?: run {
                val woff2Bytes = httpClient.get(FONT_URL).bodyAsChannel().toByteArray()
                val ttfBytes = Woff2Decoder.decodeBytes(woff2Bytes)
                    ?: error("Failed to decode WOFF2 font")
                fontBytesCache = ttfBytes
                ttfBytes
            }
        }
    }

    suspend fun loadCodepoints(): Map<String, Int> = withContext(Dispatchers.IO) {
        codepointMutex.withLock {
            codepointsCache ?: run {
                val jsonText = httpClient.get(JSON_URL).bodyAsText()
                val codepoints = json.decodeFromString<Map<String, Int>>(jsonText)
                codepointsCache = codepoints
                codepoints
            }
        }
    }

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        httpClient.get("$ICONS_BASE_URL/$iconName.svg").bodyAsText()
    }
}
