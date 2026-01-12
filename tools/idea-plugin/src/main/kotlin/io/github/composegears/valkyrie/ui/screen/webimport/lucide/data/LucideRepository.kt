package io.github.composegears.valkyrie.ui.screen.webimport.lucide.data

import androidx.collection.LruCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.composegears.valkyrie.parser.jvm.svg.SvgManipulator
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

class LucideRepository(
    private val httpClient: HttpClient,
    private val json: Json,
) {
    companion object {
        private const val UNPKG_BASE = "https://unpkg.com/lucide-static@latest"
        private const val CACHE_MAX_SIZE = 300

        private const val ATTR_STROKE_WIDTH = "stroke-width"
        private const val ATTR_WIDTH = "width"
        private const val ATTR_HEIGHT = "height"
        private const val ATTR_STROKE = "stroke"

        private const val DEFAULT_STROKE_WIDTH = "2"
        private const val DEFAULT_SIZE = "24"
        private const val DEFAULT_STROKE_COLOR = "currentColor"
    }

    private val rawSvgCache = LruCache<String, String>(CACHE_MAX_SIZE)
    private val cacheMutex = Mutex()

    suspend fun loadIconList(): List<Pair<String, LucideIconMetadata>> = withContext(Dispatchers.IO) {
        val response = httpClient.get("$UNPKG_BASE/tags.json")
        val tagsJson = json.parseToJsonElement(response.bodyAsText()) as JsonObject

        tagsJson.entries.map { (iconName, tagsArray) ->
            val tags = tagsArray.jsonArray.map { it.jsonPrimitive.content }
            iconName to LucideIconMetadata(
                tags = tags,
                categories = emptyList(), // Categories will be inferred from tags
            )
        }
    }

    suspend fun getRawSvg(iconName: String): String = withContext(Dispatchers.IO) {
        cacheMutex.withLock {
            rawSvgCache[iconName] ?: run {
                val url = "$UNPKG_BASE/icons/$iconName.svg"
                val downloaded = httpClient.get(url).bodyAsText()
                rawSvgCache.put(iconName, downloaded)
                downloaded
            }
        }
    }

    fun applySvgCustomizations(svgContent: String, settings: LucideSettings): String {
        return SvgManipulator.modifySvg(svgContent) { svgElement ->
            if (settings.strokeWidth != DEFAULT_STROKE_WIDTH.toFloat()) {
                SvgManipulator.updateAttributeRecursively(
                    element = svgElement,
                    attributeName = ATTR_STROKE_WIDTH,
                    newValue = settings.adjustedStrokeWidth().toString(),
                )
            }

            if (settings.size != DEFAULT_SIZE.toInt()) {
                svgElement.setAttribute(ATTR_WIDTH, settings.size.toString())
                svgElement.setAttribute(ATTR_HEIGHT, settings.size.toString())
            }

            if (settings.color != Color.Unspecified) {
                SvgManipulator.updateAttributeConditionally(
                    element = svgElement,
                    attributeName = ATTR_STROKE,
                    currentValue = DEFAULT_STROKE_COLOR,
                    newValue = settings.color.toHexString(),
                )
            }
        }
    }

    private fun Color.toHexString(): String {
        val argb = this.toArgb()
        return String.format(Locale.ROOT, "#%06X", 0xFFFFFF and argb)
    }
}
