package io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.data

import io.github.composegears.valkyrie.ui.screen.webimport.common.data.CodepointParser
import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.github.composegears.valkyrie.util.font.Woff2Decoder
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EvaRepository(
    private val httpClient: HttpClient,
    private val codepointParser: CodepointParser,
) {
    private val fontBytes = suspendLazy {
        withContext(Dispatchers.IO) {
            val woff2Bytes = httpClient.get(FONT_URL).bodyAsChannel().toByteArray()

            withContext(Dispatchers.Default) {
                Woff2Decoder.decodeBytes(woff2Bytes) ?: error("Failed to decode Eva Icons WOFF2 font")
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
        httpClient.get(resolveEvaSvgUrl(iconName)).bodyAsText()
    }

    private companion object {
        private const val CDN_BASE = "https://cdn.jsdelivr.net/npm/eva-icons@latest"
        private const val CSS_URL = "$CDN_BASE/style/eva-icons.css"
        private const val FONT_URL = "$CDN_BASE/style/fonts/Eva-Icons.woff2"
    }
}

internal fun resolveEvaSvgUrl(iconName: String): String {
    val stylePath = if (iconName.endsWith(EVA_OUTLINE_SUFFIX)) "outline" else "fill"
    return "https://cdn.jsdelivr.net/npm/eva-icons@latest/$stylePath/svg/$iconName.svg"
}

private const val EVA_OUTLINE_SUFFIX = "-outline"
