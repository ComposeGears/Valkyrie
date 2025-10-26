package io.github.composegears.valkyrie.ui.screen.webimport.material.data.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaterialIconsMetadata(
    @SerialName("host")
    val host: String,
    @SerialName("asset_url_pattern")
    val assetUrlPattern: String,
    @SerialName("families")
    val families: List<String>,
    @SerialName("icons")
    val icons: List<Icon>,
)

@Serializable
data class Icon(
    @SerialName("name")
    val name: String,
    @SerialName("version")
    val version: Int,
    @SerialName("popularity")
    val popularity: Int,
    @SerialName("codepoint")
    val codepoint: Int,
    @SerialName("unsupported_families")
    val unsupportedFamilies: List<String>,
    @SerialName("categories")
    val categories: List<String>,
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("sizes_px")
    val sizesPx: List<Int>,
)
