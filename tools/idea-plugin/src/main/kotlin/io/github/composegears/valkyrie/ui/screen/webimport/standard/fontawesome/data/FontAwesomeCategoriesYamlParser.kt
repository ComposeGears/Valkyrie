package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toDisplayName

class FontAwesomeCategoriesYamlParser {

    fun parse(yaml: String): Map<String, InferredCategory> {
        val categories = decodeYamlMap(yaml, FontAwesomeCategoryYamlEntry.serializer())

        val iconToCategory = linkedMapOf<String, InferredCategory>()

        categories.forEach { (categoryId, entry) ->
            val normalizedId = categoryId.trim()
            if (normalizedId.isBlank()) return@forEach

            val normalizedName = entry.label.orEmpty().trim().ifBlank { normalizedId.toDisplayName() }
            val mappedCategory = InferredCategory(id = normalizedId, name = normalizedName)

            entry.icons.map { it.trim() }
                .filter { it.isNotEmpty() }
                .forEach { iconName ->
                    iconToCategory.putIfAbsent(iconName, mappedCategory)
                }
        }

        return iconToCategory
    }
}
