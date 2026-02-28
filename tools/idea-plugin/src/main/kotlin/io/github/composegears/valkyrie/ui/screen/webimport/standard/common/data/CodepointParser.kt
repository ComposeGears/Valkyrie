package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.data

/**
 * A strategy that can extract icon name -> codepoint mapping from a CSS text.
 * Different icon providers can provide their own implementations (only differing by regex).
 */
interface CodepointParser {
    fun parse(text: String): Map<String, Int>
}
