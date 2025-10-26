package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.ui.foundation.DropdownMenu
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category

@Composable
fun CategoriesDropdown(
    selectedCategory: Category,
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onSelectCategory: (Category) -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        current = selectedCategory.name,
        values = categories.map { it.name },
        onSelect = { selectedName ->
            val selectedCategory = categories.first { it.name == selectedName }
            onSelectCategory(selectedCategory)
        },
    )
}

@Preview
@Composable
private fun CategoriesDropdownPreview() = PreviewTheme {
    CategoriesDropdown(
        selectedCategory = Category("Action"),
        categories = listOf(
            Category("Action"),
            Category("Alert"),
        ),
        onSelectCategory = {},
    )
}
