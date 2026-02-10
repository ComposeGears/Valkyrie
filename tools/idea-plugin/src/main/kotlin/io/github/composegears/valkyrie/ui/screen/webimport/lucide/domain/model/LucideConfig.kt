package io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model

data class LucideConfig(
    val gridItems: Map<Category, List<LucideIcon>>,
    val categories: List<Category>,
    val iconsByName: Map<String, LucideIcon>,
) {
    companion object {
        fun create(
            gridItems: Map<Category, List<LucideIcon>>,
            categories: List<Category>,
        ): LucideConfig {
            val iconsByName = gridItems.values.flatten().associateBy { it.name }
            return LucideConfig(
                gridItems = gridItems,
                categories = categories,
                iconsByName = iconsByName,
            )
        }
    }
}
