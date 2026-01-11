package io.github.composegears.valkyrie.uikit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.uikit.tooling.PreviewTheme
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.annotations.Nls
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.MenuScope
import org.jetbrains.jewel.ui.component.OutlinedSplitButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.component.items
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun DropdownSettingsRow(
    @Nls text: String,
    @Nls buttonText: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(start = 4.dp),
    infoText: String? = null,
    menuContent: MenuScope.() -> Unit,
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
                Text(text = "$text:")

                OutlinedSplitButton(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        val splitButtonXInRoot = coordinates.positionInRoot().x
                        val xInThisColumn = splitButtonXInRoot - offsetInRoot.x
                        offset = with(density) { xInThisColumn.toDp() }
                    },
                    onClick = {},
                    secondaryOnClick = {},
                    content = {
                        Text(text = buttonText)
                    },
                    menuContent = menuContent,
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
private fun DropdownSettingsRowPreview() = PreviewTheme {
    var currentOption by rememberMutableState { "Op 1" }

    DropdownSettingsRow(
        text = "Option name",
        buttonText = currentOption,
        infoText = "Description for the setting goes here",
        menuContent = {
            items(
                items = listOf("Op 1", "Option Option 2", "Option 3"),
                isSelected = { currentOption == it },
                onItemClick = { currentOption = it },
                content = {
                    CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(modifier = Modifier.weight(1f), text = it)
                        Tooltip(
                            tooltip = {
                                Text(text = "kek")
                            },
                        ) {
                            Icon(
                                key = AllIconsKeys.General.ContextHelp,
                                contentDescription = stringResource("accessibility.help"),
                            )
                        }
                    }
                },
            )
        },
    )
}
