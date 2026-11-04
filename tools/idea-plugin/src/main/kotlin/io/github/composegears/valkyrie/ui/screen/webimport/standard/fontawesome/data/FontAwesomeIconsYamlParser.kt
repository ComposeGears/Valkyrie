package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

class FontAwesomeIconsYamlParser {

    fun parse(yaml: String): List<FontAwesomeIconMetadata> {
        val icons = decodeYamlMap(yaml, FontAwesomeIconYamlEntry.serializer())

        return icons.mapNotNull { (name, entry) ->
            val iconName = name.trim()
            val unicodeHex = entry.unicode.trim()

            if (iconName.isBlank() || unicodeHex.isBlank() || unicodeHex.toIntOrNull(16) == null) {
                return@mapNotNull null
            }

            FontAwesomeIconMetadata(
                name = iconName,
                label = entry.label.orEmpty().trim().ifBlank { iconName },
                searchTerms = entry.search?.terms.orEmpty().map { it.trim() }.filter { it.isNotEmpty() },
                styles = entry.styles.map { it.trim() }.filter { it.isNotEmpty() },
                unicodeHex = unicodeHex,
            )
        }
    }
}
