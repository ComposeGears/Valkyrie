package io.github.composegears.valkyrie.jewel.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.DropdownList
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.annotations.Nls
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text

@OptIn(ExperimentalJewelApi::class)
@Composable
fun <T : Any> DropdownSettingsRow(
    @Nls text: String,
    current: T,
    items: List<T>,
    onSelectItem: (T) -> Unit,
    modifier: Modifier = Modifier,
    comboxModifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(start = 4.dp),
    dropdownHorizontalPadding: Dp = 0.dp,
    transform: (T) -> String = { it.toString() },
    infoText: String? = null,
) {
    val density = LocalDensity.current

    Column(modifier = modifier.padding(paddingValues)) {
        var offset by rememberMutableState { 0.dp }
        var offsetInRoot by rememberMutableState { Offset.Zero }

        Column(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                offsetInRoot = coordinates.positionInRoot()
            },
        ) {
            CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = text)
                Spacer(dropdownHorizontalPadding)
                DropdownList(
                    modifier = comboxModifier
                        .onGloballyPositioned { coordinates ->
                            val splitButtonXInRoot = coordinates.positionInRoot().x
                            val xInThisColumn = splitButtonXInRoot - offsetInRoot.x
                            offset = with(density) { xInThisColumn.toDp() }
                        },
                    items = items,
                    selected = current,
                    onSelectItem = onSelectItem,
                    transform = transform,
                )
            }

            infoText?.let {
                Spacer(8.dp)
                InfoText(
                    text = infoText,
                    modifier = Modifier.padding(start = offset),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun DropdownSettingsRowPreview() = PreviewTheme(alignment = Alignment.Center) {
    var currentOption by rememberMutableState { "Op 1" }

    DropdownSettingsRow(
        text = "Option name:",
        current = currentOption,
        onSelectItem = { currentOption = it },
        comboxModifier = Modifier.width(140.dp),
        items = listOf("Op 1", "Option Option 2", "Option 3"),
        infoText = "Description for the setting goes here",
    )
}
