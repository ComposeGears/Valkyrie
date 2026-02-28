package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data

/**
 * Simple regex-based implementation for typical provider CSS files where rules like
 * `.ri-name:before { content: "\\e900"; }` are used.
 */
open class RegexCssCodepointParser(private val regex: Regex) : CodepointParser {
    override fun parse(text: String): Map<String, Int> = regex
        .findAll(text)
        .mapNotNull { match ->
            val name = match.groupValues.getOrNull(1) ?: return@mapNotNull null
            val codepointHex = match.groupValues.getOrNull(2) ?: return@mapNotNull null
            val codepoint = codepointHex.toIntOrNull(16) ?: return@mapNotNull null

            name to codepoint
        }
        .toMap()
}
