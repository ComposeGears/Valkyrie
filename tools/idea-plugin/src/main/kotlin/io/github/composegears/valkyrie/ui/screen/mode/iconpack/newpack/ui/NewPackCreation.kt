package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.IconPackEditor
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackModeState
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

@Composable
fun NewIconPackCreation(
    state: NewPackModeState.PickedState,
    onAction: (NewPackAction) -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (InputChange) -> Unit,
) {
    val project = LocalProject.current

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
            onAddNestedPack = { onAction(NewPackAction.AddNestedPack) },
            onRemoveNestedPack = { onAction(NewPackAction.RemoveNestedPack(it)) },
        )
        Spacer(32.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            DefaultButton(
                enabled = state.inputFieldState.isValid,
                onClick = { onAction(NewPackAction.SavePack(project)) },
            ) {
                Text(text = stringResource("iconpack.newpack.creation.continue"))
            }
        }
    }
}

@Preview
@Composable
private fun NewIconPackCreationPreview() = ProjectPreviewTheme(alignment = Alignment.TopCenter) {
    NewIconPackCreation(
        state = NewPackModeState.PickedState(
            inputFieldState = InputFieldState(
                license = InputState(text = "MIT"),
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
        onAction = {},
        onValueChange = {},
    )
}
