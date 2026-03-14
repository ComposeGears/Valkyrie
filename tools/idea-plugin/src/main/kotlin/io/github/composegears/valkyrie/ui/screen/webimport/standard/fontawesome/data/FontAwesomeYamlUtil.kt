package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import it.krzeminski.snakeyaml.engine.kmp.api.Load

internal fun parseYamlMap(content: String): Map<String, Any?> = Load().loadOne(content).asYamlMap()

internal fun Any?.asYamlMap(): Map<String, Any?> = (this as? Map<*, *>).toYamlMap()

internal fun Any?.asYamlString(): String = this?.toString().orEmpty()

internal fun Any?.asYamlStringList(): List<String> = (this as? List<*>).orEmpty()
    .map { it.asYamlString().trim() }
    .filter { it.isNotEmpty() }

internal fun Map<String, Any?>.getYamlMap(key: String): Map<String, Any?> = get(key).asYamlMap()

internal fun Map<String, Any?>.getYamlString(key: String): String = get(key).asYamlString().trim()

internal fun Map<String, Any?>.getYamlStringList(key: String): List<String> = get(key).asYamlStringList()

private fun Map<*, *>?.toYamlMap(): Map<String, Any?> = this?.mapKeys { (key, _) -> key?.toString().orEmpty() }.orEmpty()
