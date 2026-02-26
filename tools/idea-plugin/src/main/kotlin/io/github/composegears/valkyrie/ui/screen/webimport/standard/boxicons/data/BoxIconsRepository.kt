package io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.data

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

class BoxIconsRepository(
    private val httpClient: HttpClient,
) {
    companion object {
        private const val UNPKG_BASE = "https://unpkg.com/boxicons@latest"
        private const val CSS_URL = "$UNPKG_BASE/css/boxicons.min.css"
        private const val FONT_WOFF2_URL = "$UNPKG_BASE/fonts/boxicons.woff2"
    }

    private val codepointsMutex = Mutex()
    private val fontMutex = Mutex()
    private var codepointsCache: List<BoxIconsCodepoint>? = null
    private var fontBytesCache: ByteArray? = null

    suspend fun loadCodepoints(): List<BoxIconsCodepoint> = withContext(Dispatchers.IO) {
        codepointsMutex.withLock {
            codepointsCache ?: run {
                val cssText = httpClient.get(CSS_URL).bodyAsText()
                parseBoxIconsCodepoints(cssText).also { codepointsCache = it }
            }
        }
    }

    suspend fun loadFontBytes(): ByteArray = withContext(Dispatchers.IO) {
        fontMutex.withLock {
            fontBytesCache ?: run {
                val decodedFont = runCatching {
                    val woff2Bytes = httpClient.get(FONT_WOFF2_URL).bodyAsChannel().toByteArray()
                    Woff2Decoder.decodeBytes(woff2Bytes)
                }.getOrNull() ?: error("Failed to decode BoxIcons WOFF2 font")

                fontBytesCache = decodedFont
                decodedFont
            }
        }
    }

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        val stylePath = when {
            iconName.startsWith("bxs-") -> "solid"
            iconName.startsWith("bxl-") -> "logos"
            else -> "regular"
        }

        httpClient.get("$UNPKG_BASE/svg/$stylePath/$iconName.svg").bodyAsText()
    }
}

internal fun parseBoxIconsCodepoints(cssText: String): List<BoxIconsCodepoint> {
    val pattern = Regex(
        """\.((?:bx|bxs|bxl)-[a-z0-9-]+)::?before\s*\{\s*content\s*:\s*"\\([A-Fa-f0-9]+)"\s*;?\s*}""",
    )

    return pattern
        .findAll(cssText)
        .mapNotNull { match ->
            val iconName = match.groupValues[1]
            val codepointHex = match.groupValues[2]
            val codepoint = codepointHex.toIntOrNull(16) ?: return@mapNotNull null
            BoxIconsCodepoint(iconName = iconName, codepoint = codepoint)
        }
        .toList()
}

data class BoxIconsCodepoint(
    val iconName: String,
    val codepoint: Int,
)
