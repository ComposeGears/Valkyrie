package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.foundation

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
import io.github.composegears.valkyrie.ui.domain.validation.ErrorCriteria
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.domain.validation.ValidationResult
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.Visibility
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.PackEditState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.ui.PackEditUi
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction.PreviewPackObject
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState

@Composable
fun NewIconPackCreation(
  state: NewPackModeState.PickedState,
  onAction: (NewPackAction) -> Unit,
  modifier: Modifier = Modifier,
  onValueChange: (InputChange) -> Unit,
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    PackEditUi(
      packEditState = state.packEditState,
      onValueChange = onValueChange,
      onAddNestedPack = { onAction(NewPackAction.AddNestedPack) },
      onRemoveNestedPack = { onAction(NewPackAction.RemoveNestedPack(it)) },
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
        onClick = { onAction(NewPackAction.SavePack) },
      ) {
        Text(text = "Export and continue")
      }
    }
  }
}

@Preview
@Composable
private fun NewIconPackCreationPreview() = PreviewTheme {
  NewIconPackCreation(
    modifier = Modifier.fillMaxWidth(0.8f),
    state = NewPackModeState.PickedState(
      packEditState = PackEditState(
        inputFieldState = InputFieldState(
          iconPackName = InputState(text = "IconPackName"),
          packageName = InputState(text = "com.example.iconpack"),
          nestedPacks = listOf(
            NestedPack(
              id = "0",
              inputFieldState = InputState(
                text = "Outlined",
                validationResult = ValidationResult.Success,
              ),
            ),
            NestedPack(
              id = "1",
              inputFieldState = InputState(
                text = "",
                validationResult = ValidationResult.Error(ErrorCriteria.EMPTY),
              ),
            ),
          ),
        ),
      ),
    ),
    onAction = {},
    onValueChange = {},
  )
}
