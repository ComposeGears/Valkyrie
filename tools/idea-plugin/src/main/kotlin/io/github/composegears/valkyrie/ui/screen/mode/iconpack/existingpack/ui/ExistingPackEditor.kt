package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.IconPackEditor
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackAction.AddNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackAction.RemoveNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackAction.SavePack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackModeState.ExistingPackEditState
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ExistingPackEditor(
    state: ExistingPackEditState,
    onValueChange: (InputChange) -> Unit,
    onAction: (ExistingPackAction) -> Unit,
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
            onAddNestedPack = { onAction(AddNestedPack) },
            onRemoveNestedPack = { onAction(RemoveNestedPack(it)) },
        )
        Spacer(32.dp)
        DefaultButton(
            modifier = Modifier.align(Alignment.End),
            onClick = { onAction(SavePack) },
            enabled = state.inputFieldState.isValid,
        ) {
            Text(text = stringResource("iconpack.existingpack.editor.update.continue"))
        }
    }
}

@Preview
@Composable
private fun ExistingPackEditorPreview() = PreviewTheme {
    ExistingPackEditor(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        state = ExistingPackEditState(),
        onValueChange = {},
        onAction = {},
    )
}
