package io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.DropdownList
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.WebImportTopActions
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.Category
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            DropdownList(
                modifier = Modifier.width(140.dp),
                selected = selectedCategory,
                items = categories,
                transform = { it.name },
                onSelectItem = onSelectCategory,
            )
        },
    )
}

@Preview
@Composable
private fun MaterialTopActionsPreview() = PreviewTheme {
    var category by rememberMutableState { Category("1", "Accessibility") }

    LucideTopActions(
        categories = listOf(
            Category("1", "Accessibility"),
            Category("2", "Communication"),
            Category("3", "Tools"),
        ),
        onSelectCategory = { category = it },
        selectedCategory = category,
        onToggleCustomization = {},
        onSearchQueryChange = {},
    )
}
