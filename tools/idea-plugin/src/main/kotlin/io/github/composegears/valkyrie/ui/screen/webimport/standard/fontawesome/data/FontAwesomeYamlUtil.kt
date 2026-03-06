package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer

private val yaml = Yaml(configuration = YamlConfiguration(strictMode = false))

internal fun <T> decodeYamlMap(content: String, valueSerializer: KSerializer<T>): Map<String, T> {
    return yaml.decodeFromString(
        deserializer = MapSerializer(String.serializer(), valueSerializer),
        string = content,
    )
}

internal fun List<String>.cleanNonBlank(): List<String> {
    return map { it.trim() }.filter { it.isNotBlank() }
}
