package io.github.composegears.valkyrie.ui.screen.webimport.svg.octicons.data

import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class OcticonsRepository(
    private val httpClient: HttpClient,
    private val json: Json,
    private val metadataParser: OcticonsMetadataParser,
) {
    private val latestVersion = suspendLazy {
        withContext(Dispatchers.IO) {
            json.parseToJsonElement(httpClient.get(PACKAGE_JSON_URL).bodyAsText())
                .jsonObject["version"]
                ?.jsonPrimitive
                ?.content
                ?: error("Failed to resolve Octicons package version")
        }
    }

    private val metadata = suspendLazy {
        withContext(Dispatchers.IO) {
            metadataParser.parse(httpClient.get(resolveMetadataUrl(latestVersion())).bodyAsText())
        }
    }

    suspend fun loadMetadata(): List<OcticonsIconMetadata> = metadata()

    suspend fun downloadSvg(path: String): String = withContext(Dispatchers.IO) {
        httpClient.get(resolveOcticonsSvgUrl(path, version = latestVersion())).bodyAsText()
    }

    private companion object {
        private const val PACKAGE_JSON_URL = "https://cdn.jsdelivr.net/npm/@primer/octicons@latest/package.json"

        private fun resolveMetadataUrl(version: String): String {
            return "https://cdn.jsdelivr.net/npm/@primer/octicons@$version/build/data.json"
        }
    }
}
