package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.button.OutlineIconButton
import io.github.composegears.valkyrie.jewel.textfield.validation.ValidationResult
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.IconPackName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.IconPackNameValidation
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.NestedPackName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.NestedPackNameValidation
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.PackageName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.PackageNameValidation
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.PackEditState
import io.github.composegears.valkyrie.util.stringResource
import kotlin.random.Random
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Link
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun IconPackEditor(
    packEditState: PackEditState,
    onValueChange: (InputChange) -> Unit,
    onAddNestedPack: () -> Unit,
    modifier: Modifier = Modifier,
    onRemoveNestedPack: (NestedPack) -> Unit,
) {
    val inputFieldState = packEditState.inputFieldState

    Column(modifier = modifier) {
        val packageName = inputFieldState.packageName
        val iconPackName = inputFieldState.iconPackName

        CodeTooltipHeader(
            text = stringResource("iconpack.editor.package"),
            highlights = buildPackPackageHighlight(
                packageName = packageName.text,
                iconPackName = iconPackName.text,
            ),
        )
        Spacer(8.dp)
        PackageTextField(
            modifier = Modifier.width(200.dp),
            text = packageName.text,
            onValueChange = { onValueChange(PackageName(it)) },
            onValidationChange = { onValueChange(PackageNameValidation(it)) },
        )
        Spacer(32.dp)
        CodeTooltipHeader(
            text = stringResource("iconpack.editor.iconpack.name"),
            highlights = buildIconPackHighlight(iconPackName.text),
        )
        Spacer(8.dp)
        IconPackTextField(
            modifier = Modifier.width(200.dp),
            enabled = iconPackName.enabled,
            text = iconPackName.text,
            onValueChange = { onValueChange(IconPackName(it)) },
            onValidationChange = { onValueChange(IconPackNameValidation(it)) },
        )

        if (inputFieldState.nestedPacks.isEmpty()) {
            Spacer(8.dp)
            AddPackButton(
                modifier = Modifier.align(Alignment.Start),
                onClick = onAddNestedPack,
            )
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
    Column(modifier = modifier.padding(start = 32.dp)) {
        Spacer(8.dp)
        nestedPacks.forEachIndexed { index, nestedPack ->
            val inputFieldState = nestedPack.inputFieldState

            Row(modifier = Modifier.fillMaxWidth()) {
                IconPackTextField(
                    modifier = Modifier.width(200.dp),
                    enabled = inputFieldState.enabled,
                    text = inputFieldState.text,
                    onValueChange = { onValueChange(NestedPackName(id = nestedPack.id, text = it)) },
                    onValidationChange = {
                        onValueChange(
                            NestedPackNameValidation(
                                id = nestedPack.id,
                                validationResult = it,
                            ),
                        )
                    },
                )
                Spacer(8.dp)
                OutlineIconButton(
                    key = AllIconsKeys.General.Delete,
                    onClick = { onRemove(nestedPack) },
                    contentDescription = stringResource("accessibility.remove"),
                    enabled = inputFieldState.enabled
                )
            }
            if (index != nestedPacks.lastIndex) {
                Spacer(8.dp)
            }
        }
        if (allowAddNestedPack) {
            Spacer(8.dp)
            AddPackButton(onClick = onAddNestedPack)
        }
    }
}

@Composable
private fun AddPackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Link(
        modifier = modifier,
        text = stringResource("iconpack.editor.add.nested.pack"),
        onClick = onClick,
    )
}

@Preview
@Composable
private fun IconPackEditorPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    var state by rememberMutableState {
        PackEditState(
            inputFieldState = InputFieldState(
                packageName = InputState(text = "com.example.iconpack"),
                iconPackName = InputState(text = "ValkyrieIcons"),
                nestedPacks = listOf(
                    NestedPack(
                        id = "0",
                        inputFieldState = InputState(
                            text = "Outlined",
                            enabled = false,
                        ),
                    ),
                    NestedPack(
                        id = "1",
                        inputFieldState = InputState(),
                    ),
                ),
            ),
        )
    }

    IconPackEditor(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        packEditState = state,
        onValueChange = {
            when (it) {
                is PackageName -> state = state.copy(
                    inputFieldState = state.inputFieldState.copy(
                        packageName = state.inputFieldState.packageName.copy(text = it.text),
                    ),
                )
                is PackageNameValidation -> state = state.copy(
                    inputFieldState = state.inputFieldState.copy(
                        packageName = state.inputFieldState.packageName.copy(
                            isValid = it.validationResult is ValidationResult.Success,
                        ),
                    ),
                )
                is IconPackName -> state = state.copy(
                    inputFieldState = state.inputFieldState.copy(
                        iconPackName = state.inputFieldState.iconPackName.copy(text = it.text),
                    ),
                )
                is IconPackNameValidation -> state = state.copy(
                    inputFieldState = state.inputFieldState.copy(
                        iconPackName = state.inputFieldState.iconPackName.copy(
                            isValid = it.validationResult is ValidationResult.Success,
                        ),
                    ),
                )
                is NestedPackName -> {
                    val updatedNestedPacks = state.inputFieldState.nestedPacks.map { nestedPack ->
                        when (nestedPack.id) {
                            it.id -> nestedPack.copy(
                                inputFieldState = nestedPack.inputFieldState.copy(text = it.text),
                            )
                            else -> nestedPack
                        }
                    }
                    state = state.copy(inputFieldState = state.inputFieldState.copy(nestedPacks = updatedNestedPacks))
                }
                is NestedPackNameValidation -> {
                    val updatedNestedPacks = state.inputFieldState.nestedPacks.map { nestedPack ->
                        when (nestedPack.id) {
                            it.id -> nestedPack.copy(
                                inputFieldState = nestedPack.inputFieldState.copy(
                                    isValid = it.validationResult is ValidationResult.Success,
                                ),
                            )
                            else -> nestedPack
                        }
                    }
                    state = state.copy(inputFieldState = state.inputFieldState.copy(nestedPacks = updatedNestedPacks))
                }
            }
        },
        onAddNestedPack = {
            val newPack = NestedPack(
                id = Random.nextLong().toString(),
                inputFieldState = InputState(),
            )
            val updatedNestedPacks = state.inputFieldState.nestedPacks + newPack
            state = state.copy(inputFieldState = state.inputFieldState.copy(nestedPacks = updatedNestedPacks))
        },
        onRemoveNestedPack = { pack ->
            val updatedNestedPacks = state.inputFieldState.nestedPacks.filter { pack.id != it.id }
            state = state.copy(inputFieldState = state.inputFieldState.copy(nestedPacks = updatedNestedPacks))
        },
    )

    Text(
        modifier = Modifier
            .align(alignment = Alignment.TopEnd)
            .padding(end = 16.dp),
        text = "Input valid: ${state.inputFieldState.isValid}",
    )
}
