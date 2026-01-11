package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.foundation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.domain.validation.ErrorCriteria
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.domain.validation.ValidationResult
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.InfoItem
import io.github.composegears.valkyrie.ui.foundation.icons.ArrowDropDown
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
        Spacer(32.dp)
        AdditionalOptions(
            useMaterialPack = state.useMaterialPack,
            onChangeUseMaterialPack = {
                onAction(NewPackAction.UseMaterialPack(it))
            },
        )
        Spacer(32.dp)
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
                Text(text = "Continue")
            }
        }
    }
}

@Composable
private fun AdditionalOptions(
    useMaterialPack: Boolean,
    onChangeUseMaterialPack: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                shape = RoundedCornerShape(10.dp),
            ),
    ) {
        var expanded by rememberMutableState { false }
        val angle by animateFloatAsState(if (expanded) 180f else 0f)
        CenterVerticalRow(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        expanded = !expanded
                    },
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Additional options",
                style = MaterialTheme.typography.bodySmall,
            )
            Icon(
                modifier = Modifier.rotate(angle),
                imageVector = ValkyrieIcons.ArrowDropDown,
                contentDescription = null,
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp),
            visible = expanded,
        ) {
            InfoItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .toggleable(
                        value = useMaterialPack,
                        onValueChange = onChangeUseMaterialPack,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ),
                title = "Use Material Icon pack",
                description = "Will be used existing pack from \"androidx.compose.material.icons\" package",
                content = {
                    Switch(
                        modifier = Modifier.scale(0.9f),
                        checked = useMaterialPack,
                        onCheckedChange = onChangeUseMaterialPack,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun NewIconPackCreationPreview() = PreviewTheme {
    NewIconPackCreation(
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
            useMaterialPack = true,
        ),
        onAction = {},
        modifier = Modifier.fillMaxWidth(0.8f),
        onValueChange = {},
    )
}
