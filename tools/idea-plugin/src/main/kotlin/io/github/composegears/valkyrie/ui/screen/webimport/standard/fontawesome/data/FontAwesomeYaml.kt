package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.charleskorn.kaml.YamlMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

// Font Awesome metadata contains many extra fields, so unknown keys are ignored;
// malformed or non-map documents decode to an empty map instead of failing the import
internal val fontAwesomeYaml = Yaml(configuration = YamlConfiguration(strictMode = false))

/**
 * Decodes a Font Awesome YAML document into a map keyed by entry name.
 *
 * Malformed or non-map documents yield an empty map; individual entries that
 * fail to deserialize are skipped instead of failing the whole document.
 */
internal fun <T> decodeYamlMap(content: String, deserializer: KSerializer<T>): Map<String, T> {
    val root = runCatching { fontAwesomeYaml.parseToYamlNode(content) }.getOrNull() as? YamlMap ?: return emptyMap()
    val result = linkedMapOf<String, T>()
    for ((key, value) in root.entries) {
        val decoded = runCatching { fontAwesomeYaml.decodeFromYamlNode(deserializer, value) }.getOrNull() ?: continue
        result[key.content] = decoded
    }
    return result
}

@Serializable
internal data class FontAwesomeIconYamlEntry(
    val label: String? = null,
    val search: Search? = null,
    val styles: List<String?> = emptyList(),
    val unicode: String = "",
) {
    @Serializable
    data class Search(val terms: List<String?> = emptyList())
}

@Serializable
internal data class FontAwesomeCategoryYamlEntry(
    val label: String? = null,
    val icons: List<String?> = emptyList(),
)
