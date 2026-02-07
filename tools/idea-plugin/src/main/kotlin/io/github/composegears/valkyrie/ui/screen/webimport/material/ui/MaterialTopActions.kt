package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

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
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.Category
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.IconFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview

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
                modifier = Modifier.width(100.dp),
                fontFamily = iconFontFamily,
                onSelectFontFamily = onSelectFontFamily,
            )
            DropdownList(
                modifier = Modifier.width(100.dp),
                selected = selectedCategory,
                items = categories,
                transform = { it.name },
                onSelectItem = onSelectCategory,
                maxPopupWidth = 120.dp,
            )
        },
    )
}

@Preview
@Composable
private fun MaterialTopActionsPreview() = PreviewTheme {
    var category by rememberMutableState { Category("Action") }
    var fontFamily by rememberMutableState { IconFontFamily.OUTLINED }

    MaterialTopActions(
        selectedCategory = category,
        categories = listOf(
            Category("Action"),
            Category("Alert"),
            Category("Audio & Video"),
        ),
        iconFontFamily = fontFamily,
        onToggleSidePanel = {},
        onSelectFontFamily = { fontFamily = it },
        onSelectCategory = { category = it },
        onSearchQueryChange = {},
    )
}
