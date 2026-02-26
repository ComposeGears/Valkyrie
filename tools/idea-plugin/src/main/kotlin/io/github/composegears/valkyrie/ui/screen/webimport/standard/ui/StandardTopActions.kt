package io.github.composegears.valkyrie.ui.screen.webimport.standard.ui

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
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.IconStyle
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.InferredCategory
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StandardTopActions(
    categories: List<InferredCategory>,
    styles: List<IconStyle>,
    selectedCategory: InferredCategory,
    selectedStyle: IconStyle?,
    onToggleCustomization: () -> Unit,
    onSelectStyle: (IconStyle) -> Unit,
    onSelectCategory: (InferredCategory) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    WebImportTopActions(
        modifier = modifier,
        onToggleCustomization = onToggleCustomization,
        onSearchQueryChange = onSearchQueryChange,
        actionsContent = {
            if (styles.isNotEmpty()) {
                val resolvedSelectedStyle = selectedStyle ?: styles.first()
                DropdownList(
                    modifier = Modifier.width(120.dp),
                    selected = resolvedSelectedStyle,
                    items = styles,
                    transform = { it.name },
                    onSelectItem = onSelectStyle,
                    maxPopupWidth = 140.dp,
                )
            }
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
private fun StandardTopActionsPreview() = PreviewTheme {
    var category by rememberMutableState { InferredCategory("accessibility", "Accessibility") }
    var style by rememberMutableState { IconStyle("regular", "Regular") }

    StandardTopActions(
        categories = listOf(
            InferredCategory("accessibility", "Accessibility"),
            InferredCategory("communication", "Communication"),
            InferredCategory("tools", "Tools"),
        ),
        styles = listOf(
            IconStyle("regular", "Regular"),
            IconStyle("solid", "Solid"),
        ),
        onSelectStyle = { style = it },
        selectedStyle = style,
        onSelectCategory = { category = it },
        selectedCategory = category,
        onToggleCustomization = {},
        onSearchQueryChange = {},
    )
}
