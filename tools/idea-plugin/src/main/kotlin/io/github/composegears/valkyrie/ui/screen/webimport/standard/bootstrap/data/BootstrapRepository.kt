package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data.CodepointParser
import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.github.composegears.valkyrie.util.font.Woff2Decoder
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BootstrapRepository(
    private val httpClient: HttpClient,
    private val codepointParser: CodepointParser,
) {
    companion object {
        private const val CDN_BASE = "https://cdn.jsdelivr.net/npm/bootstrap-icons@latest"
        private const val FONT_URL = "$CDN_BASE/font/fonts/bootstrap-icons.woff2"
        private const val CSS_URL = "$CDN_BASE/font/bootstrap-icons.min.css"
        private const val ICONS_BASE_URL = "$CDN_BASE/icons"
    }

    private val fontBytes = suspendLazy {
        withContext(Dispatchers.IO) {
            val woff2Bytes = httpClient.get(FONT_URL).bodyAsChannel().toByteArray()

            withContext(Dispatchers.Default) {
                Woff2Decoder.decodeBytes(woff2Bytes) ?: error("Failed to decode WOFF2 font")
            }
        }
    }

    private val codepoints = suspendLazy {
        withContext(Dispatchers.IO) {
            val cssText = httpClient.get(CSS_URL).bodyAsText()
            codepointParser.parse(cssText)
        }
    }

    suspend fun loadFontBytes(): ByteArray = fontBytes()

    suspend fun loadCodepoints(): Map<String, Int> = codepoints()

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        httpClient.get("$ICONS_BASE_URL/$iconName.svg").bodyAsText()
    }
}
