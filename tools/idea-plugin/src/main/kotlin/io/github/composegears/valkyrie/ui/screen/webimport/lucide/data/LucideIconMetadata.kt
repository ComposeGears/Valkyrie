package io.github.composegears.valkyrie.ui.screen.webimport.lucide.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LucideIconMetadata(
    @SerialName("\$schema")
    val schema: String? = null,
    @SerialName("contributors")
    val contributors: List<String> = emptyList(),
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("categories")
    val categories: List<String>,
)
