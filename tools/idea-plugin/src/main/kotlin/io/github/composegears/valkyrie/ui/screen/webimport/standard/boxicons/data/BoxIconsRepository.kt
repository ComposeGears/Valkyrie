package io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.data

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

class BoxIconsRepository(
    private val httpClient: HttpClient,
    private val codepointParser: CodepointParser,
) {
    companion object {
        private const val CDN_BASE = "https://cdn.jsdelivr.net/npm/boxicons@latest"
        private const val CSS_URL = "$CDN_BASE/css/boxicons.min.css"
        private const val FONT_URL = "$CDN_BASE/fonts/boxicons.woff2"
    }

    private val codepoints = suspendLazy {
        withContext(Dispatchers.IO) {
            val cssText = httpClient.get(CSS_URL).bodyAsText()
            codepointParser.parse(cssText)
        }
    }

    private val fontBytes = suspendLazy {
        withContext(Dispatchers.IO) {
            val woff2Bytes = httpClient.get(FONT_URL).bodyAsChannel().toByteArray()

            withContext(Dispatchers.Default) {
                Woff2Decoder.decodeBytes(woff2Bytes) ?: error("Failed to decode BoxIcons WOFF2 font")
            }
        }
    }

    suspend fun loadCodepoints(): Map<String, Int> = codepoints()

    suspend fun loadFontBytes(): ByteArray = fontBytes()

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        val stylePath = when {
            iconName.startsWith("bxs-") -> "solid"
            iconName.startsWith("bxl-") -> "logos"
            else -> "regular"
        }

        httpClient.get("$CDN_BASE/svg/$stylePath/$iconName.svg").bodyAsText()
    }
}
