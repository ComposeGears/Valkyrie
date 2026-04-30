package io.github.composegears.valkyrie.ui.screen.webimport.standard.feather.data

import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeatherRepository(
    private val httpClient: HttpClient,
    private val glyphMapParser: FeatherGlyphMapParser,
) {
    companion object {
        private const val VECTOR_ICONS_CDN_BASE =
            "https://cdn.jsdelivr.net/npm/@react-native-vector-icons/feather@latest"
        private const val GLYPHMAP_URL = "$VECTOR_ICONS_CDN_BASE/glyphmaps/Feather.json"
        private const val FONT_URL = "$VECTOR_ICONS_CDN_BASE/fonts/Feather.ttf"
    }

    private val codepoints = suspendLazy {
        withContext(Dispatchers.IO) {
            glyphMapParser.parse(httpClient.get(GLYPHMAP_URL).bodyAsText())
        }
    }

    private val fontBytes = suspendLazy {
        withContext(Dispatchers.IO) {
            httpClient.get(FONT_URL).bodyAsChannel().toByteArray()
        }
    }

    suspend fun loadCodepoints(): Map<String, Int> = codepoints()

    suspend fun loadFontBytes(): ByteArray = fontBytes()

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        httpClient.get(resolveFeatherSvgUrl(iconName)).bodyAsText()
    }
}
