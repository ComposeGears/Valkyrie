package io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.data.font

import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.domain.model.MaterialFontSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.domain.model.MaterialIconFontFamily
import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.github.composegears.valkyrie.util.font.Woff2Decoder
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.toByteArray
import kotlin.math.abs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MaterialFontRepository(
    private val httpClient: HttpClient,
) {
    companion object {
        private const val SVG_BASE_URL = "https://fonts.gstatic.com/s/i/short-term/release"
    }

    private val fontBytesCache = MaterialIconFontFamily.entries.associateWith { family ->
        suspendLazy { loadFontBytes(family.cdnUrl) }
    }

    suspend fun loadFont(fontFamily: MaterialIconFontFamily): ByteArray = fontBytesCache[fontFamily]!!()

    suspend fun downloadSvg(
        name: String,
        fontFamily: String,
        fontSettings: MaterialFontSettings,
    ): String = withContext(Dispatchers.IO) {
        val url = "$SVG_BASE_URL/$fontFamily/$name/${fontSettings.iconOption()}/${fontSettings.iconSize()}.svg"
        httpClient.get(url).bodyAsText()
    }

    private suspend fun loadFontBytes(url: String): ByteArray = withContext(Dispatchers.IO) {
        val woff2Bytes = httpClient.get(url).bodyAsChannel().toByteArray()
        withContext(Dispatchers.Default) {
            Woff2Decoder.decodeBytes(woff2Bytes) ?: error("Failed to decode WOFF2 font: $url")
        }
    }

    private fun MaterialFontSettings.iconOption(): String {
        if (!isModified) {
            return "default"
        }

        return buildString {
            if (weight != 400) {
                append("wght$weight")
            }
            if (grade != 0) {
                when {
                    grade < 0 -> append("gradN${abs(grade)}")
                    else -> append("grad$grade")
                }
            }
            if (fill) {
                append("fill1")
            }
        }
    }

    private fun MaterialFontSettings.iconSize() = "${opticalSize.toInt()}px"
}
