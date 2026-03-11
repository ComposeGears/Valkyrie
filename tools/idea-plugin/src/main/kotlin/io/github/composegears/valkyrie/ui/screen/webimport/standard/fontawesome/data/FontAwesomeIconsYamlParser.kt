package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import kotlinx.serialization.Serializable

class FontAwesomeIconsYamlParser {

    fun parse(yaml: String): List<FontAwesomeIconMetadata> {
        val root = decodeYamlMap(yaml, FontAwesomeIconYaml.serializer())

        return root.mapNotNull { (name, icon) ->
            val iconName = name.trim()
            val unicodeHex = icon.unicode.trim()

            if (iconName.isBlank() || unicodeHex.isBlank() || unicodeHex.toIntOrNull(16) == null) {
                return@mapNotNull null
            }

            val styles = icon.styles.cleanNonBlank()
            val searchTerms = icon.search.terms.cleanNonBlank()

            FontAwesomeIconMetadata(
                name = iconName,
                label = icon.label.trim().ifBlank { iconName },
                searchTerms = searchTerms,
                styles = styles,
                unicodeHex = unicodeHex,
            )
        }
    }

    @Serializable
    private data class FontAwesomeIconYaml(
        val label: String = "",
        val search: Search = Search(),
        val styles: List<String> = emptyList(),
        val unicode: String = "",
    )

    @Serializable
    private data class Search(val terms: List<String> = emptyList())
}
