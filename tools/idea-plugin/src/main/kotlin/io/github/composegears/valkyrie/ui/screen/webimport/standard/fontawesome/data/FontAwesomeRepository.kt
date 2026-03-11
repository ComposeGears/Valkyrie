package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.InferredCategory
import io.github.composegears.valkyrie.util.coroutines.suspendLazy
import io.github.composegears.valkyrie.util.font.Woff2Decoder
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FontAwesomeRepository(
    private val httpClient: HttpClient,
    private val iconsYamlParser: FontAwesomeIconsYamlParser,
    private val categoriesYamlParser: FontAwesomeCategoriesYamlParser,
) {
    companion object {
        private const val CDN_BASE = "https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@latest"
        private const val ICONS_YML_URL = "$CDN_BASE/metadata/icons.yml"
        private const val CATEGORIES_YML_URL = "$CDN_BASE/metadata/categories.yml"
        private const val SOLID_STYLE_ID = "solid"
        private const val REGULAR_STYLE_ID = "regular"
        private const val BRANDS_STYLE_ID = "brands"
        private const val FONT_SOLID = "$CDN_BASE/webfonts/fa-solid-900.woff2"
        private const val FONT_REGULAR = "$CDN_BASE/webfonts/fa-regular-400.woff2"
        private const val FONT_BRANDS = "$CDN_BASE/webfonts/fa-brands-400.woff2"
    }

    private val fontsByStyleId = mapOf(
        SOLID_STYLE_ID to suspendLazy { loadAndDecodeWoff2(FONT_SOLID) },
        REGULAR_STYLE_ID to suspendLazy { loadAndDecodeWoff2(FONT_REGULAR) },
        BRANDS_STYLE_ID to suspendLazy { loadAndDecodeWoff2(FONT_BRANDS) },
    )

    private val iconsMetadata = suspendLazy {
        withContext(Dispatchers.IO) {
            val yaml = httpClient.get(ICONS_YML_URL).bodyAsText()
            iconsYamlParser.parse(yaml)
        }
    }

    private val categoriesByIcon = suspendLazy {
        withContext(Dispatchers.IO) {
            val yaml = httpClient.get(CATEGORIES_YML_URL).bodyAsText()
            categoriesYamlParser.parse(yaml)
        }
    }

    suspend fun loadFontBytes(styleId: String): ByteArray {
        val loader = fontsByStyleId[styleId] ?: fontsByStyleId.getValue(SOLID_STYLE_ID)
        return loader()
    }

    suspend fun loadIconsMetadata(): List<FontAwesomeIconMetadata> = iconsMetadata()

    suspend fun loadCategoriesByIcon(): Map<String, InferredCategory> = categoriesByIcon()

    suspend fun downloadSvg(iconName: String, styleId: String): String = withContext(Dispatchers.IO) {
        httpClient.get("$CDN_BASE/svgs/$styleId/$iconName.svg").bodyAsText()
    }

    private suspend fun loadAndDecodeWoff2(url: String): ByteArray {
        return withContext(Dispatchers.IO) {
            val woff2Bytes = httpClient.get(url).bodyAsChannel().toByteArray()

            withContext(Dispatchers.Default) {
                Woff2Decoder.decodeBytes(woff2Bytes) ?: error("Failed to decode WOFF2 font from: $url")
            }
        }
    }
}
