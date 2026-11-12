package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toDisplayName

class FontAwesomeCategoriesYamlParser {

    /**
     * Parses Font Awesome categories into a map from icon name to inferred category.
     *
     * The first category wins when an icon appears in several; empty category ids are skipped.
     */
    fun parse(yaml: String): Map<String, InferredCategory> {
        val categories = decodeYamlMap(yaml, FontAwesomeCategoryYamlEntry.serializer())

        val iconToCategory = linkedMapOf<String, InferredCategory>()

        categories.forEach { (categoryId, entry) ->
            val normalizedId = categoryId.trim()
            if (normalizedId.isBlank()) return@forEach

            val normalizedName = entry.label.orEmpty().trim().ifBlank { normalizedId.toDisplayName() }
            val mappedCategory = InferredCategory(id = normalizedId, name = normalizedName)

            entry.icons.filterNotNull().map { it.trim() }
                .filter { it.isNotEmpty() }
                .forEach { iconName ->
                    iconToCategory.putIfAbsent(iconName, mappedCategory)
                }
        }

        return iconToCategory
    }
}
