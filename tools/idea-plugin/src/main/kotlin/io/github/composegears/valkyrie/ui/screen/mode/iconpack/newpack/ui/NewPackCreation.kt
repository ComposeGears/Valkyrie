package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.settings.CheckboxSettingsRow
import io.github.composegears.valkyrie.jewel.settings.ExpandedGroup
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.IconPackEditor
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.PackEditState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun NewIconPackCreation(
    state: NewPackModeState.PickedState,
    onAction: (NewPackAction) -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (InputChange) -> Unit,
) {
    Column(
        modifier = modifier
            .widthIn(max = 450.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconPackEditor(
            modifier = Modifier.fillMaxWidth(),
            packEditState = state.packEditState,
            onValueChange = onValueChange,
            onAddNestedPack = { onAction(NewPackAction.AddNestedPack) },
            onRemoveNestedPack = { onAction(NewPackAction.RemoveNestedPack(it)) },
        )
        Spacer(32.dp)
        ExpandedGroup(
            text = stringResource("iconpack.newpack.creation.additional.options"),
            paddingValues = PaddingValues(0.dp),
        ) {
            CheckboxSettingsRow(
                text = stringResource("iconpack.newpack.creation.use.material.pack"),
                infoText = stringResource("iconpack.newpack.creation.use.material.pack.description"),
                checked = state.useMaterialPack,
                onCheckedChange = { onAction(NewPackAction.UseMaterialPack(it)) },
            )
        }
        Spacer(32.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            DefaultButton(
                enabled = state.nextAvailable,
                onClick = { onAction(NewPackAction.SavePack) },
            ) {
                Text(text = stringResource("iconpack.newpack.creation.continue"))
            }
        }
    }
}

@Preview
@Composable
private fun NewIconPackCreationPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    NewIconPackCreation(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        state = NewPackModeState.PickedState(
            packEditState = PackEditState(
                inputFieldState = InputFieldState(
                    iconPackName = InputState(text = "ValkyrieIcons"),
                    packageName = InputState(text = "com.example.iconpack"),
                    nestedPacks = listOf(
                        NestedPack(
                            id = "0",
                            inputFieldState = InputState(text = "Outlined"),
                        ),
                        NestedPack(
                            id = "1",
                            inputFieldState = InputState(),
                        ),
                    ),
                ),
            ),
            useMaterialPack = true,
        ),
        onAction = {},
        onValueChange = {},
    )
}
