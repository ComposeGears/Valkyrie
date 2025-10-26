package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.outlined.Search
import io.github.composegears.valkyrie.compose.icons.outlined.Tune
import io.github.composegears.valkyrie.compose.util.subtle
import io.github.composegears.valkyrie.ui.foundation.IconButtonSmall
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
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
    val onSearchQueryChangeRemembered by rememberUpdatedState(onSearchQueryChange)

    var searchQuery by rememberSaveable { mutableStateOf("") }

    val actions = remember(selectedCategory, iconFontFamily) {
        movableContentOf {
            IconButtonSmall(
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.subtle(),
                        shape = CircleShape,
                    ),
                imageVector = ValkyrieIcons.Outlined.Tune,
                onClick = onToggleSidePanel,
                iconSize = 18.dp,
            )
            FontFamilyDropdown(
                fontFamily = iconFontFamily,
                onSelectFontFamily = onSelectFontFamily,
            )
            CategoriesDropdown(
                selectedCategory = selectedCategory,
                categories = categories,
                onSelectCategory = onSelectCategory,
            )
        }
    }

    val searchInput = remember {
        movableContentOf { modifier: Modifier ->
            SearchInputField(
                modifier = modifier,
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearchQueryChangeRemembered(it)
                },
                label = "Search icons",
                leadingIcon = {
                    Icon(
                        imageVector = ValkyrieIcons.Outlined.Search,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = ValkyrieIcons.Close,
                        contentDescription = null,
                    )
                },
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
    ) {
        val isCompact = maxWidth < 400.dp

        if (isCompact) {
            Column(modifier = Modifier.fillMaxWidth()) {
                CenterVerticalRow(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    actions()
                }
                VerticalSpacer(8.dp)
                searchInput(
                    Modifier
                        .widthIn(min = 300.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        } else {
            CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                actions()
                WeightSpacer()
                searchInput(Modifier.widthIn(min = 200.dp))
            }
        }
    }
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
