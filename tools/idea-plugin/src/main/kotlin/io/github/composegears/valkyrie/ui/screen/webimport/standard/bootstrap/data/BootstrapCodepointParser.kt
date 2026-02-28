package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data.CodepointParser
import kotlinx.serialization.json.Json

class BootstrapCodepointParser(
    private val json: Json,
) : CodepointParser {

    override fun parse(text: String): Map<String, Int> {
        return json.decodeFromString<Map<String, Int>>(text)
    }
}
