package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.domain.validation.ErrorCriteria
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.domain.validation.ValidationResult
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.InputField
import io.github.composegears.valkyrie.ui.foundation.InputTextField
import io.github.composegears.valkyrie.ui.foundation.icons.Delete
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.PackEditState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.util.buildIconPackHighlight
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.util.buildPackPackageHighlight

@Composable
fun PackEditUi(
    packEditState: PackEditState,
    onValueChange: (InputChange) -> Unit,
    onAddNestedPack: () -> Unit,
    modifier: Modifier = Modifier,
    onRemoveNestedPack: (NestedPack) -> Unit,
) {
    val inputFieldState = packEditState.inputFieldState

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconPackName = inputFieldState.iconPackName
        val packageName = inputFieldState.packageName

        InputField(
            modifier = Modifier.fillMaxWidth(),
            enabled = packageName.enabled,
            caption = "Package",
            value = packageName.text,
            isError = packageName.validationResult is ValidationResult.Error,
            highlights = buildPackPackageHighlight(
                packageName = packageName.text,
                iconPackName = iconPackName.text,
            ),
            onValueChange = { onValueChange(InputChange.PackageName(it)) },
            supportingText = if (packageName.validationResult is ValidationResult.Error) {
                {
                    Text(
                        text = when (packageName.validationResult.errorCriteria) {
                            ErrorCriteria.EMPTY -> "Value can't be empty"
                            ErrorCriteria.INCONSISTENT_FORMAT -> "Invalid package"
                            ErrorCriteria.FIRST_LETTER_LOWER_CASE -> error("not possible")
                        },
                    )
                }
            } else {
                null
            },
        )
        VerticalSpacer(32.dp)

        InputField(
            modifier = Modifier.fillMaxWidth(),
            enabled = iconPackName.enabled,
            caption = "Icon pack name",
            value = iconPackName.text,
            highlights = buildIconPackHighlight(iconPackName.text),
            isError = iconPackName.validationResult is ValidationResult.Error,
            onValueChange = { onValueChange(InputChange.IconPackName(it)) },
            supportingText = if (iconPackName.validationResult is ValidationResult.Error) {
                {
                    Text(
                        text = when (iconPackName.validationResult.errorCriteria) {
                            ErrorCriteria.EMPTY -> "Value can't be empty"
                            ErrorCriteria.INCONSISTENT_FORMAT -> "Invalid name"
                            ErrorCriteria.FIRST_LETTER_LOWER_CASE -> "First letter should be uppercase"
                        },
                    )
                }
            } else {
                null
            },
        )
        if (inputFieldState.nestedPacks.isEmpty()) {
            TextButton(
                modifier = Modifier.align(Alignment.Start),
                onClick = onAddNestedPack,
            ) {
                Text(text = "+ Add nested pack")
            }
        } else {
            NestedPacks(
                nestedPacks = inputFieldState.nestedPacks,
                allowAddNestedPack = inputFieldState.allowAddNestedPack,
                onRemove = onRemoveNestedPack,
                onValueChange = onValueChange,
                onAddNestedPack = onAddNestedPack,
            )
        }
    }
}

@Composable
private fun NestedPacks(
    nestedPacks: List<NestedPack>,
    allowAddNestedPack: Boolean,
    onRemove: (NestedPack) -> Unit,
    onAddNestedPack: () -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (InputChange) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        VerticalSpacer(8.dp)
        nestedPacks.forEachIndexed { index, nestedPack ->
            val inputFieldState = nestedPack.inputFieldState

            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier.padding(top = 6.dp),
                    enabled = inputFieldState.enabled,
                    imageVector = ValkyrieIcons.Delete,
                    onClick = { onRemove(nestedPack) },
                    iconSize = 18.dp,
                )
                InputTextField(
                    modifier = Modifier.weight(1f),
                    enabled = inputFieldState.enabled,
                    value = inputFieldState.text,
                    isError = inputFieldState.validationResult is ValidationResult.Error,
                    onValueChange = {
                        onValueChange(InputChange.NestedPackName(id = nestedPack.id, text = it))
                    },
                    supportingText = if (inputFieldState.validationResult is ValidationResult.Error) {
                        {
                            Text(
                                text = when (inputFieldState.validationResult.errorCriteria) {
                                    ErrorCriteria.EMPTY -> "Value can't be empty"
                                    ErrorCriteria.INCONSISTENT_FORMAT -> "Invalid name"
                                    ErrorCriteria.FIRST_LETTER_LOWER_CASE -> "First letter should be uppercase"
                                },
                            )
                        }
                    } else {
                        null
                    },
                )
            }
            if (index != nestedPacks.lastIndex) {
                VerticalSpacer(8.dp)
            }
        }
        TextButton(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 40.dp),
            onClick = onAddNestedPack,
            enabled = allowAddNestedPack,
        ) {
            Text(text = "+ Add nested pack")
        }
    }
}

@Preview
@Composable
private fun PackEditUiPreview() = PreviewTheme {
    PackEditUi(
        modifier = Modifier.fillMaxWidth(0.8f),
        packEditState = PackEditState(
            inputFieldState = InputFieldState(
                iconPackName = InputState(text = "IconPackName"),
                packageName = InputState(text = "com.example.iconpack", enabled = false),
                nestedPacks = listOf(
                    NestedPack(
                        id = "0",
                        inputFieldState = InputState(
                            text = "Outlined",
                            enabled = false,
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
        onValueChange = {},
        onAddNestedPack = {},
        onRemoveNestedPack = {},
    )
}
