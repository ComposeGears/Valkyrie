package io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.data

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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class RemixRepository(
    private val httpClient: HttpClient,
    private val json: Json,
    private val codepointParser: CodepointParser,
) {
    companion object {
        private const val CDN_BASE = "https://cdn.jsdelivr.net/npm/remixicon@latest"
        private const val PACKAGE_JSON_URL = "$CDN_BASE/package.json"
        private const val FONT_URL = "$CDN_BASE/fonts/remixicon.woff2"
        private const val CSS_URL = "$CDN_BASE/fonts/remixicon.css"
        private const val FLAT_INDEX_URL_TEMPLATE = "https://data.jsdelivr.com/v1/package/npm/remixicon@%s/flat"
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

    private val svgMetadata = suspendLazy {
        withContext(Dispatchers.IO) {
            val flatIndexJson = httpClient.get(loadFlatIndexUrl()).bodyAsText()
            val (svgPathByName, categoryByName) = parseSvgMetadata(flatIndexJson)
            RemixSvgMetadata(
                svgPathByName = svgPathByName,
                categoryByName = categoryByName,
            )
        }
    }

    suspend fun loadFontBytes(): ByteArray = fontBytes()

    private val remixVersion = suspendLazy {
        withContext(Dispatchers.IO) {
            val packageJson = httpClient.get(PACKAGE_JSON_URL).bodyAsText()
            json.parseToJsonElement(packageJson)
                .jsonObject["version"]
                ?.jsonPrimitive
                ?.content
                ?: error("Failed to resolve Remix package version")
        }
    }

    suspend fun loadIconList(): Map<String, RemixIconMetadata> {
        val codepoints = loadCodepoints()
        val categoriesByName = runCatching { loadSvgMetadata().categoryByName }
            .getOrDefault(emptyMap())

        return codepoints.mapValues { (iconName, codepoint) ->
            RemixIconMetadata(
                codepoint = codepoint,
                categoryName = categoriesByName[iconName],
            )
        }
    }

    suspend fun downloadSvg(iconName: String): String = withContext(Dispatchers.IO) {
        val iconPath = loadSvgMetadata().svgPathByName[iconName]
            ?: error("SVG path not found for Remix icon: $iconName")

        httpClient.get("$CDN_BASE/$iconPath").bodyAsText()
    }

    private suspend fun loadCodepoints(): Map<String, Int> = codepoints()

    private suspend fun loadSvgMetadata(): RemixSvgMetadata = svgMetadata()

    private suspend fun loadFlatIndexUrl(): String {
        val version = loadPackageVersion()
        return FLAT_INDEX_URL_TEMPLATE.format(version)
    }

    private suspend fun loadPackageVersion(): String = remixVersion()

    private fun parseSvgMetadata(jsonText: String): Pair<Map<String, String>, Map<String, String>> {
        val root = json.parseToJsonElement(jsonText).jsonObject
        val files = root["files"]?.jsonArray.orEmpty()

        val svgPathByName = mutableMapOf<String, String>()
        val categoryByName = mutableMapOf<String, String>()
        files.forEach { fileElement ->
            val filePath = fileElement.jsonObject["name"]?.jsonPrimitive?.content ?: return@forEach
            if (!filePath.startsWith("/icons/") || !filePath.endsWith(".svg")) return@forEach

            val pathParts = filePath.split('/')
            val providerCategory = pathParts.getOrNull(2)

            val iconName = filePath.substringAfterLast('/').removeSuffix(".svg")
            if (iconName.isBlank()) return@forEach

            svgPathByName.putIfAbsent(iconName, filePath.removePrefix("/"))
            if (!providerCategory.isNullOrBlank()) {
                categoryByName.putIfAbsent(iconName, providerCategory)
            }
        }

        return svgPathByName to categoryByName
    }
}

data class RemixIconMetadata(
    val codepoint: Int,
    val categoryName: String?,
)

private data class RemixSvgMetadata(
    val svgPathByName: Map<String, String>,
    val categoryByName: Map<String, String>,
)
