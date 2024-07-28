package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.Visibility
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.ui.PackEditUi
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.AddNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.PreviewPackObject
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.RemoveNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction.SavePack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackModeState.ExistingPackEditState

@Composable
fun ExistingPackEditor(
    state: ExistingPackEditState,
    onValueChange: (InputChange) -> Unit,
    onAction: (ExistingPackAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PackEditUi(
            packEditState = state.packEditState,
            onValueChange = onValueChange,
            onAddNestedPack = { onAction(AddNestedPack) },
            onRemoveNestedPack = { onAction(RemoveNestedPack(it)) },
        )
        VerticalSpacer(32.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            IconButton(
                imageVector = ValkyrieIcons.Visibility,
                onClick = { onAction(PreviewPackObject) },
                enabled = state.nextAvailable,
            )
            Button(
                enabled = state.nextAvailable,
                onClick = { onAction(SavePack) },
            ) {
                Text(text = "Update and continue")
            }
        }
    }
}

@Preview
@Composable
private fun ExistingPackEditorPreview() = PreviewTheme {
    ExistingPackEditor(
        modifier = Modifier.fillMaxWidth(0.8f),
        state = ExistingPackEditState(),
        onValueChange = {},
        onAction = {},
    )
}
