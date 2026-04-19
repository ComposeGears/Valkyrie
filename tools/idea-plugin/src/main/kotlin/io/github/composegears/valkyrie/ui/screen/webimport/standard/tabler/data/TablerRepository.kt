package io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data.CodepointParser
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.domain.TABLER_FILLED_STYLE
import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.github.composegears.valkyrie.util.font.Woff2Decoder
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TablerRepository(
    private val httpClient: HttpClient,
    private val codepointParser: CodepointParser,
) {
    private val outlineFontBytes = suspendLazy { loadAndDecodeWoff2(OUTLINE_FONT_URL) }
    private val filledFontBytes = suspendLazy { loadAndDecodeWoff2(FILLED_FONT_URL) }

    private val outlineCodepoints = suspendLazy { loadCodepoints(OUTLINE_CSS_URL) }
    private val filledCodepoints = suspendLazy { loadCodepoints(FILLED_CSS_URL) }

    suspend fun loadOutlineCodepoints(): Map<String, Int> = outlineCodepoints()

    suspend fun loadFilledCodepoints(): Map<String, Int> = filledCodepoints()

    suspend fun loadFontBytes(style: IconStyle?): ByteArray {
        return if (style?.id == TABLER_FILLED_STYLE.id) {
            filledFontBytes()
        } else {
            outlineFontBytes()
        }
    }

    suspend fun downloadSvg(iconName: String, style: IconStyle): String = withContext(Dispatchers.IO) {
        httpClient.get(resolveTablerSvgUrl(iconName, style)).bodyAsText()
    }

    private suspend fun loadCodepoints(url: String): Map<String, Int> = withContext(Dispatchers.IO) {
        val cssText = httpClient.get(url).bodyAsText()
        codepointParser.parse(cssText)
    }

    private suspend fun loadAndDecodeWoff2(url: String): ByteArray = withContext(Dispatchers.IO) {
        val woff2Bytes = httpClient.get(url).bodyAsChannel().toByteArray()
        withContext(Dispatchers.Default) {
            Woff2Decoder.decodeBytes(woff2Bytes) ?: error("Failed to decode Tabler WOFF2 font from: $url")
        }
    }

    private companion object {
        private const val WEBFONT_CDN_BASE = "https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/dist"
        private const val OUTLINE_CSS_URL = "$WEBFONT_CDN_BASE/tabler-icons.min.css"
        private const val FILLED_CSS_URL = "$WEBFONT_CDN_BASE/tabler-icons-filled.min.css"
        private const val OUTLINE_FONT_URL = "$WEBFONT_CDN_BASE/fonts/tabler-icons.woff2"
        private const val FILLED_FONT_URL = "$WEBFONT_CDN_BASE/fonts/tabler-icons-filled.woff2"
    }
}

internal fun resolveTablerSvgUrl(iconName: String, style: IconStyle): String {
    val stylePath = if (style.id == TABLER_FILLED_STYLE.id) "filled" else "outline"
    return "https://cdn.jsdelivr.net/npm/@tabler/icons@latest/icons/$stylePath/$iconName.svg"
}
