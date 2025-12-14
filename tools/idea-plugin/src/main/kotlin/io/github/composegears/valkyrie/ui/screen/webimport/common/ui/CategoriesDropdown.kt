package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.ui.foundation.DropdownMenu
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

/**
 * Generic category dropdown that works with any category type
 */
@Composable
fun <T> CategoriesDropdown(
    selectedCategory: T,
    categories: List<T>,
    categoryName: (T) -> String,
    onSelectCategory: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        modifier = modifier,
        current = categoryName(selectedCategory),
        values = categories.map(categoryName),
        onSelect = { selectedName ->
            val selected = categories.first { categoryName(it) == selectedName }
            onSelectCategory(selected)
        },
    )
}

@Preview
@Composable
private fun CategoriesDropdownPreview() = PreviewTheme {
    data class TestCategory(val name: String)

    CategoriesDropdown(
        selectedCategory = TestCategory("All"),
        categories = listOf(
            TestCategory("All"),
            TestCategory("Arrows"),
            TestCategory("Text"),
        ),
        categoryName = { it.name },
        onSelectCategory = {},
    )
}
