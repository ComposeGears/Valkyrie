package io.github.composegears.valkyrie.ui.screen.webimport.standard.ionicons.data

import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IoniconsRepository(
    private val httpClient: HttpClient,
    private val glyphMapParser: IoniconsGlyphMapParser,
    private val metadataParser: IoniconsMetadataParser,
) {
    private val fontBytes = suspendLazy {
        withContext(Dispatchers.IO) {
            httpClient.get(FONT_URL).bodyAsChannel().toByteArray()
        }
    }

    private val codepoints = suspendLazy {
        withContext(Dispatchers.IO) {
            glyphMapParser.parse(httpClient.get(GLYPHMAP_URL).bodyAsText())
        }
    }

    private val metadata = suspendLazy {
        withContext(Dispatchers.IO) {
            metadataParser.parse(httpClient.get(METADATA_URL).bodyAsText())
        }
    }

    suspend fun loadFontBytes(): ByteArray = fontBytes()

    suspend fun loadCodepoints(): Map<String, Int> = codepoints()

    suspend fun loadMetadata(): List<IoniconsIconMetadata> = metadata()

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        httpClient.get(resolveIoniconsSvgUrl(iconName)).bodyAsText()
    }

    private companion object {
        private const val RN_CDN_BASE = "https://cdn.jsdelivr.net/npm/@react-native-vector-icons/ionicons@latest"
        private const val IONICONS_CDN_BASE = "https://cdn.jsdelivr.net/npm/ionicons@latest"
        private const val GLYPHMAP_URL = "$RN_CDN_BASE/glyphmaps/Ionicons.json"
        private const val FONT_URL = "$RN_CDN_BASE/fonts/Ionicons.ttf"
        private const val METADATA_URL = "$IONICONS_CDN_BASE/dist/ionicons.json"
    }
}
