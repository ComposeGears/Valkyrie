package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.data

import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain.toDisplayName
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.InferredCategory
import kotlinx.serialization.Serializable

class FontAwesomeCategoriesYamlParser {

    fun parse(yaml: String): Map<String, InferredCategory> {
        val root = decodeYamlMap(yaml, FontAwesomeCategoryYaml.serializer())

        val iconToCategory = linkedMapOf<String, InferredCategory>()

        root.forEach { (categoryId, category) ->
            val normalizedId = categoryId.trim()
            if (normalizedId.isBlank()) return@forEach

            val normalizedName = category.label.trim().ifBlank { normalizedId.toDisplayName() }
            val mappedCategory = InferredCategory(id = normalizedId, name = normalizedName)

            category.icons
                .cleanNonBlank()
                .forEach { iconName ->
                    iconToCategory.putIfAbsent(iconName, mappedCategory)
                }
        }

        return iconToCategory
    }

    @Serializable
    private data class FontAwesomeCategoryYaml(
        val icons: List<String> = emptyList(),
        val label: String = "",
    )
}
