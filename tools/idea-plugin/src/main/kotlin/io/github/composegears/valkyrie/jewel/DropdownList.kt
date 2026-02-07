package io.github.composegears.valkyrie.jewel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.lazy.rememberSelectableLazyListState
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.ListComboBox
import org.jetbrains.jewel.ui.component.Text

@OptIn(ExperimentalJewelApi::class)
@Composable
fun <T : Any> DropdownList(
    selected: T,
    items: List<T>,
    transform: (T) -> String,
    onSelectItem: (T) -> Unit,
    modifier: Modifier = Modifier,
    maxPopupWidth: Dp = Dp.Unspecified,
) {
    val updatedTransform by rememberUpdatedState(transform)

    // TODO: Remove workaround in IDEA 2027 🌚
    // https://youtrack.jetbrains.com/issue/JEWEL-1244/ListComboBox-saves-the-selected-index-internally-even-if-it-is-reset-externally
    val listState = rememberSelectableLazyListState()

    LaunchedEffect(selected) {
        listState.selectedKeys = setOf(updatedTransform(selected))
    }

    ListComboBox(
        items = items.map(transform),
        selectedIndex = items.indexOf(selected),
        onSelectedItemChange = { onSelectItem(items[it]) },
        maxPopupWidth = maxPopupWidth,
        modifier = modifier,
        itemKeys = { _, item -> item },
        listState = listState,
    )
}

@Preview
@Composable
private fun DropdownListPreview() = PreviewTheme(alignment = Alignment.Center) {
    var selectedCategory by rememberMutableState { 1 }

    Column {
        DropdownList(
            modifier = Modifier.width(100.dp),
            selected = selectedCategory,
            items = listOf(1, 2, 3, 4, 5),
            transform = { it.toString() },
            onSelectItem = { selectedCategory = it },
        )
        Spacer(16.dp)
        DefaultButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { selectedCategory = 1 },
        ) {
            Text("Reset")
        }
    }
}
