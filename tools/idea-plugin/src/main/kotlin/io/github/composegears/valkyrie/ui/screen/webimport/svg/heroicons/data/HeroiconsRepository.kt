package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.data

import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class HeroiconsRepository(
    private val httpClient: HttpClient,
    private val json: Json,
    private val indexParser: HeroiconsIndexParser,
) {
    private val latestVersion = suspendLazy {
        withContext(Dispatchers.IO) {
            json.parseToJsonElement(httpClient.get(PACKAGE_JSON_URL).bodyAsText())
                .jsonObject["version"]
                ?.jsonPrimitive
                ?.content
                ?: error("Failed to resolve Heroicons package version")
        }
    }

    private val metadata = suspendLazy {
        withContext(Dispatchers.IO) {
            indexParser.parse(httpClient.get(resolveFlatIndexUrl(latestVersion())).bodyAsText())
        }
    }

    suspend fun loadMetadata(): List<HeroiconsIconMetadata> = metadata()

    suspend fun downloadSvg(path: String): String = withContext(Dispatchers.IO) {
        httpClient.get(resolveHeroiconsSvgUrl(path, version = latestVersion())).bodyAsText()
    }

    private companion object {
        private const val PACKAGE_JSON_URL = "https://cdn.jsdelivr.net/npm/heroicons@latest/package.json"

        private fun resolveFlatIndexUrl(version: String): String {
            return "https://data.jsdelivr.com/v1/package/npm/heroicons@$version/flat"
        }
    }
}
