package io.github.composegears.valkyrie.ui.screen.webimport.lucide.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LucideIconMetadata(
    @SerialName("contributors")
    val contributors: List<String> = emptyList(),
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("categories")
    val categories: List<String>,
)
