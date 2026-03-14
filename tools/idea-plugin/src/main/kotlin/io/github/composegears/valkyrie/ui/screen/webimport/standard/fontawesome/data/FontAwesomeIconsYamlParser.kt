package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

class FontAwesomeIconsYamlParser {

    fun parse(yaml: String): List<FontAwesomeIconMetadata> {
        val root = parseYamlMap(yaml)

        return root.mapNotNull { (name, icon) ->
            val iconMap = icon.asYamlMap()
            val iconName = name.trim()
            val unicodeHex = iconMap.getYamlString("unicode")

            if (iconName.isBlank() || unicodeHex.isBlank() || unicodeHex.toIntOrNull(16) == null) {
                return@mapNotNull null
            }

            val styles = iconMap.getYamlStringList("styles")
            val searchTerms = iconMap.getYamlMap("search").getYamlStringList("terms")

            FontAwesomeIconMetadata(
                name = iconName,
                label = iconMap.getYamlString("label").ifBlank { iconName },
                searchTerms = searchTerms,
                styles = styles,
                unicodeHex = unicodeHex,
            )
        }
    }
}
