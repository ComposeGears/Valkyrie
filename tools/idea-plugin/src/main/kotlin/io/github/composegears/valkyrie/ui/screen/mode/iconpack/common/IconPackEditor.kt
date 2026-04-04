package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.button.OutlineIconButton
import io.github.composegears.valkyrie.jewel.textarea.TextArea
import io.github.composegears.valkyrie.jewel.textfield.validation.ValidationResult
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.IconPackName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.IconPackNameValidation
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.License
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.NestedPackName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.NestedPackNameValidation
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.PackageName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange.PackageNameValidation
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import io.github.composegears.valkyrie.util.stringResource
import kotlin.random.Random
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Link
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun IconPackEditor(
    inputFieldState: InputFieldState,
    onValueChange: (InputChange) -> Unit,
    onAddNestedPack: () -> Unit,
    modifier: Modifier = Modifier,
    showLicense: Boolean = true,
    onRemoveNestedPack: (NestedPack) -> Unit,
) {
    Column(modifier = modifier) {
        val license = inputFieldState.license
        val packageName = inputFieldState.packageName
        val iconPackName = inputFieldState.iconPackName

        if (showLicense) {
            Text(stringResource("iconpack.editor.license"))
            Spacer(8.dp)
            TextArea(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                text = license.text,
                onValueChange = { onValueChange(License(it)) },
                placeholder = stringResource("iconpack.existingpack.editor.license.placeholder"),
                enabled = license.enabled,
            )
            Spacer(32.dp)
        }

        CodeTooltipHeader(
            text = stringResource("iconpack.editor.package"),
            code = buildPackPackageHighlight(
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
            enabled = packageName.enabled,
        )
        Spacer(32.dp)
        CodeTooltipHeader(
            text = stringResource("iconpack.editor.iconpack.name"),
            code = buildIconPackHighlight(iconPackName.text),
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
                allowEditing = inputFieldState.allowEditing,
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
    allowEditing: Boolean,
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
                    enabled = inputFieldState.enabled,
                )
            }
            if (index != nestedPacks.lastIndex) {
                Spacer(8.dp)
            }
        }
        if (allowEditing) {
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
private fun IconPackEditorPreview() = ProjectPreviewTheme(alignment = Alignment.TopCenter) {
    var state by rememberMutableState {
        InputFieldState(
            license = InputState(),
            packageName = InputState(text = "com.example.iconpack", enabled = false),
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
        )
    }

    IconPackEditor(
        modifier = Modifier
            .widthIn(max = 450.dp)
            .padding(horizontal = 16.dp),
        inputFieldState = state,
        onValueChange = {
            when (it) {
                is License -> state = state.copy(
                    license = state.license.copy(text = it.text),
                )
                is PackageName -> state = state.copy(
                    packageName = state.packageName.copy(text = it.text),
                )
                is PackageNameValidation -> state = state.copy(
                    packageName = state.packageName.copy(
                        isValid = it.validationResult is ValidationResult.Success,
                    ),
                )
                is IconPackName -> state = state.copy(
                    iconPackName = state.iconPackName.copy(text = it.text),
                )
                is IconPackNameValidation -> state = state.copy(
                    iconPackName = state.iconPackName.copy(
                        isValid = it.validationResult is ValidationResult.Success,
                    ),
                )
                is NestedPackName -> {
                    val updatedNestedPacks = state.nestedPacks.map { nestedPack ->
                        when (nestedPack.id) {
                            it.id -> nestedPack.copy(
                                inputFieldState = nestedPack.inputFieldState.copy(text = it.text),
                            )
                            else -> nestedPack
                        }
                    }
                    state = state.copy(nestedPacks = updatedNestedPacks)
                }
                is NestedPackNameValidation -> {
                    val updatedNestedPacks = state.nestedPacks.map { nestedPack ->
                        when (nestedPack.id) {
                            it.id -> nestedPack.copy(
                                inputFieldState = nestedPack.inputFieldState.copy(
                                    isValid = it.validationResult is ValidationResult.Success,
                                ),
                            )
                            else -> nestedPack
                        }
                    }
                    state = state.copy(nestedPacks = updatedNestedPacks)
                }
            }
        },
        onAddNestedPack = {
            val newPack = NestedPack(
                id = Random.nextLong().toString(),
                inputFieldState = InputState(),
            )
            val updatedNestedPacks = state.nestedPacks + newPack
            state = state.copy(nestedPacks = updatedNestedPacks)
        },
        onRemoveNestedPack = { pack ->
            val updatedNestedPacks = state.nestedPacks.filter { pack.id != it.id }
            state = state.copy(nestedPacks = updatedNestedPacks)
        },
    )

    Text(
        modifier = Modifier
            .align(alignment = Alignment.BottomEnd)
            .padding(end = 8.dp, bottom = 8.dp),
        text = "Input valid: ${state.isValid}",
    )
}