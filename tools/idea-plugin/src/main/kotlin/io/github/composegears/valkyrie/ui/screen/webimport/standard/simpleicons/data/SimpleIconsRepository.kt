package io.github.composegears.valkyrie.ui.screen.webimport.standard.simpleicons.data

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
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class SimpleIconsRepository(
    private val httpClient: HttpClient,
    private val json: Json,
    private val metadataParser: SimpleIconsMetadataParser,
) {
    private val latestVersion = suspendLazy {
        withContext(Dispatchers.IO) {
            json.parseToJsonElement(httpClient.get(PACKAGE_JSON_URL).bodyAsText())
                .jsonObject["version"]
                ?.jsonPrimitive
                ?.content
                ?: error("Failed to resolve Simple Icons package version")
        }
    }

    private val metadata = suspendLazy {
        withContext(Dispatchers.IO) {
            metadataParser.parse(httpClient.get(resolveMetadataUrl(latestVersion())).bodyAsText())
        }
    }

    private val fontBytes = suspendLazy {
        withContext(Dispatchers.IO) {
            val woff2Bytes = httpClient.get(resolveFontUrl(latestVersion())).bodyAsChannel().toByteArray()
            withContext(Dispatchers.Default) {
                Woff2Decoder.decodeBytes(woff2Bytes) ?: error("Failed to decode Simple Icons WOFF2 font")
            }
        }
    }

    suspend fun loadMetadata(): List<SimpleIconMetadata> = metadata()

    suspend fun loadFontBytes(): ByteArray = fontBytes()

    suspend fun downloadSvg(slug: String): String = withContext(Dispatchers.IO) {
        httpClient.get(resolveSimpleIconsSvgUrl(slug, version = latestVersion())).bodyAsText()
    }

    private companion object {
        private const val PACKAGE_JSON_URL = "https://cdn.jsdelivr.net/npm/simple-icons-font@latest/package.json"

        private fun resolveMetadataUrl(version: String): String {
            return "https://cdn.jsdelivr.net/npm/simple-icons-font@$version/font/simple-icons.min.json"
        }

        private fun resolveFontUrl(version: String): String {
            return "https://cdn.jsdelivr.net/npm/simple-icons-font@$version/font/SimpleIcons.woff2"
        }
    }
}
