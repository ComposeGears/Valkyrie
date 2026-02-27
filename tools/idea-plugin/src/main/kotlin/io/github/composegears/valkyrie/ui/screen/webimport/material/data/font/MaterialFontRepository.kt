package io.github.composegears.valkyrie.ui.screen.webimport.material.data.font

import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialFontSettings
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
    suspend fun downloadFont(url: String): ByteArray = withContext(Dispatchers.IO) {
        val woff2Bytes = httpClient.get(url).bodyAsChannel().toByteArray()

        Woff2Decoder.decodeBytes(woff2Bytes) ?: error("Failed to decode WOFF2 font")
    }

    suspend fun downloadSvg(
        name: String,
        fontFamily: String,
        fontSettings: MaterialFontSettings,
    ): String = withContext(Dispatchers.IO) {
        val iconOption = fontSettings.iconOption()
        val iconSize = fontSettings.iconSize()
        val url = "https://fonts.gstatic.com/s/i/short-term/release/$fontFamily/$name/$iconOption/$iconSize.svg"

        httpClient.get(url).bodyAsText()
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
