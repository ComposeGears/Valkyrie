package io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.CategoriesDropdown
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.WebImportTopActions
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.Category

@Composable
fun LucideTopActions(
    categories: List<Category>,
    selectedCategory: Category,
    onToggleCustomization: () -> Unit,
    onSelectCategory: (Category) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    WebImportTopActions(
        modifier = modifier,
        onToggleCustomization = onToggleCustomization,
        onSearchQueryChange = onSearchQueryChange,
        actionsContent = {
            CategoriesDropdown(
                selectedCategory = selectedCategory,
                categories = categories,
                categoryName = { it.title },
                onSelectCategory = onSelectCategory,
            )
        },
    )
}
