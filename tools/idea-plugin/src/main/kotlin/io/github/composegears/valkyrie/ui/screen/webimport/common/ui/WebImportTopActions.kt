package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.button.OutlineIconButton
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.painter.hints.Stateful
import org.jetbrains.jewel.ui.theme.textFieldStyle

/**
 * Generic top actions bar for web import screens.
 * Provides a consistent layout with a customization button, custom actions content, and a search field.
 *
 * @param onToggleCustomization Callback when the customization button is clicked
 * @param onSearchQueryChange Callback when the search query changes
 * @param modifier Modifier to be applied to the root container
 * @param actionsContent Custom content to display between the customization button and search field (e.g., dropdowns)
 */
@Composable
fun WebImportTopActions(
    onToggleCustomization: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    actionsContent: @Composable () -> Unit,
) {
    val onSearchQueryChangeRemembered by rememberUpdatedState(onSearchQueryChange)

    val actions = remember {
        movableContentOf {
            OutlineIconButton(
                key = AllIconsKeys.Actions.Properties,
                contentDescription = stringResource("accessibility.properties"),
                onClick = onToggleCustomization,
            )
            actionsContent()
        }
    }

    val searchInput = remember {
        movableContentOf { modifier: Modifier ->
            val state = rememberTextFieldState("")

            LaunchedEffect(state) {
                snapshotFlow {
                    state.text.toString()
                }.collectLatest {
                    onSearchQueryChangeRemembered(it)
                }
            }
            TextField(
                modifier = modifier,
                state = state,
                placeholder = {
                    Text(text = stringResource("web.import.search.placeholder"))
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        key = AllIconsKeys.Actions.Find,
                        contentDescription = stringResource("accessibility.search"),
                    )
                },
                trailingIcon = if (state.text.isNotEmpty()) {
                    {
                        IconButton(
                            onClick = { state.setTextAndPlaceCursorAtEnd("") },
                            style = JewelTheme.textFieldStyle.iconButtonStyle,
                        ) { state ->
                            Icon(
                                key = AllIconsKeys.General.Close,
                                contentDescription = stringResource("accessibility.clear"),
                                hint = Stateful(state),
                            )
                        }
                    }
                } else {
                    null
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
                Spacer(8.dp)
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
private fun WebImportTopActionsPreview() = PreviewTheme {
    WebImportTopActions(
        onToggleCustomization = {},
        onSearchQueryChange = {},
        actionsContent = {
            Text(text = "Content")
        },
    )
}
