package io.github.composegears.valkyrie.parser.unified.util

import io.github.composegears.valkyrie.parser.unified.ext.capitalized
import io.github.composegears.valkyrie.parser.unified.ext.removePrefix

object IconNameFormatter {

    private val invalidCharacterRegex = "[^a-zA-Z0-9\\-_ ]".toRegex()
    private val camelCaseRegex = "(?<!^)(?=[A-Z])|(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)".toRegex()

    fun format(name: String): String = name
        .trimStart('-', '_')
        .removeSuffix(".svg")
        .removeSuffix(".xml")
        .removePrefix("ic_")
        .removePrefix("ic-")
        .replace(invalidCharacterRegex, "_")
        .split(camelCaseRegex)
        .joinToString(separator = "") { it.capitalized() }
        .split("_", "-")
        .joinToString(separator = "") { it.capitalized() }
}
