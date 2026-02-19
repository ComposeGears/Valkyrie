package io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.ui

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
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.settings.CheckboxSettingsRow
import io.github.composegears.valkyrie.jewel.settings.Group
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.IconPackEditor
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackAction.UpdateFlatPackageStructure
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackState
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun MaterialPackCreation(
    state: MaterialPackState.PickedState,
    onAction: (MaterialPackAction) -> Unit,
    onValueChange: (InputChange) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .widthIn(max = 450.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconPackEditor(
            modifier = Modifier.fillMaxWidth(),
            inputFieldState = state.inputFieldState,
            onValueChange = onValueChange,
            onAddNestedPack = { },
            onRemoveNestedPack = { },
        )
        Spacer(32.dp)
        Group(
            paddingValues = PaddingValues(0.dp),
            text = stringResource("iconpack.material.additional.options"),
        ) {
            CheckboxSettingsRow(
                text = stringResource("iconpack.material.flat.package.structure"),
                checked = state.flatPackageStructure,
                onCheckedChange = { onAction(UpdateFlatPackageStructure(it)) },
            )
        }
        Spacer(32.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            DefaultButton(
                onClick = { onAction(MaterialPackAction.SavePack) },
                enabled = state.inputFieldState.isValid,
            ) {
                Text(text = stringResource("iconpack.newpack.creation.continue"))
            }
        }
    }
}

@Preview
@Composable
private fun MaterialPackCreationPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    MaterialPackCreation(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        state = MaterialPackState.PickedState(
            inputFieldState = InputFieldState(
                iconPackName = InputState(text = "Icons", enabled = false),
                packageName = InputState(text = "androidx.compose.material.icons", enabled = false),
                nestedPacks = listOf(
                    NestedPack(id = "0", inputFieldState = InputState(text = "Filled", enabled = false)),
                    NestedPack(id = "1", inputFieldState = InputState(text = "Outlined", enabled = false)),
                ),
                allowEditing = false,
            ),
        ),
        onAction = {},
        onValueChange = {},
    )
}
