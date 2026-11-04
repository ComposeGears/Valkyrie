package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer

// Font Awesome metadata contains many extra fields, so unknown keys are ignored;
// malformed or non-map documents decode to an empty map instead of failing the import
internal val fontAwesomeYaml = Yaml(configuration = YamlConfiguration(strictMode = false))

internal fun <T> decodeYamlMap(content: String, deserializer: KSerializer<T>): Map<String, T> = runCatching {
    fontAwesomeYaml.decodeFromString(MapSerializer(String.serializer(), deserializer), content)
}.getOrDefault(emptyMap())

@Serializable
internal data class FontAwesomeIconYamlEntry(
    val label: String? = null,
    val search: Search? = null,
    val styles: List<String> = emptyList(),
    val unicode: String = "",
) {
    @Serializable
    data class Search(val terms: List<String> = emptyList())
}

@Serializable
internal data class FontAwesomeCategoryYamlEntry(
    val label: String? = null,
    val icons: List<String> = emptyList(),
)
