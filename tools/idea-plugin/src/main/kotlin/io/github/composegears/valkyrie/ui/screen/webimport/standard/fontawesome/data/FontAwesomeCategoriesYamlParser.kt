package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import io.github.composegears.valkyrie.ui.screen.webimport.common.domain.category.InferredCategory
import io.github.composegears.valkyrie.ui.screen.webimport.common.util.toDisplayName

class FontAwesomeCategoriesYamlParser {

    fun parse(yaml: String): Map<String, InferredCategory> {
        val root = parseYamlMap(yaml)

        val iconToCategory = linkedMapOf<String, InferredCategory>()

        root.forEach { (categoryId, category) ->
            val categoryMap = category.asYamlMap()
            val normalizedId = categoryId.trim()
            if (normalizedId.isBlank()) return@forEach

            val normalizedName = categoryMap.getYamlString("label").ifBlank { normalizedId.toDisplayName() }
            val mappedCategory = InferredCategory(id = normalizedId, name = normalizedName)

            categoryMap.getYamlStringList("icons")
                .forEach { iconName ->
                    iconToCategory.putIfAbsent(iconName, mappedCategory)
                }
        }

        return iconToCategory
    }
}
