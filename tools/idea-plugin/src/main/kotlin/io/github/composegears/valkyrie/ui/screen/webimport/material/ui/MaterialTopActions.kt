package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.CategoriesDropdown
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.WebImportTopActions
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.IconFontFamily

@Composable
fun MaterialTopActions(
    categories: List<Category>,
    selectedCategory: Category,
    iconFontFamily: IconFontFamily,
    onToggleSidePanel: () -> Unit,
    onSelectFontFamily: (IconFontFamily) -> Unit,
    onSelectCategory: (Category) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    WebImportTopActions(
        modifier = modifier,
        onToggleCustomization = onToggleSidePanel,
        onSearchQueryChange = onSearchQueryChange,
        actionsContent = {
            FontFamilyDropdown(
                fontFamily = iconFontFamily,
                onSelectFontFamily = onSelectFontFamily,
            )
            CategoriesDropdown(
                selectedCategory = selectedCategory,
                categories = categories,
                categoryName = { it.name },
                onSelectCategory = onSelectCategory,
            )
        },
    )
}

@Preview
@Composable
private fun MaterialTopActionsPreview() = PreviewTheme {
    MaterialTopActions(
        selectedCategory = Category("Action"),
        categories = listOf(
            Category("Action"),
            Category("Alert"),
        ),
        iconFontFamily = IconFontFamily.OUTLINED,
        onToggleSidePanel = {},
        onSelectFontFamily = {},
        onSelectCategory = {},
        onSearchQueryChange = {},
    )
}
