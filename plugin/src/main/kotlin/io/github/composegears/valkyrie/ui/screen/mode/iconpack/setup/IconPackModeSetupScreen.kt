package io.github.composegears.valkyrie.ui.screen.mode.iconpack.setup

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import io.github.composegears.valkyrie.ui.domain.validation.ErrorCriteria
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.domain.validation.ValidationResult
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.InputField
import io.github.composegears.valkyrie.ui.foundation.InputTextField
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.setup.InputChange.IconPackName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.setup.InputChange.NestedPackName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.preview.IconPackPreviewScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.setup.util.getIconPackAnnotatedString
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.setup.util.getPackageAnnotatedString
import kotlinx.coroutines.Dispatchers

val IconPackModeSetupScreen by navDestination<Unit> {

    val navController = navController()
    val viewModel = koinTiamatViewModel<IconPackModeSetupViewModel>()

    val state by viewModel.state.collectAsState(Dispatchers.Main.immediate)

    IconPackModeSetupUI(
        state = state,
        onValueChange = viewModel::onValueChange,
        onBack = {
            navController.back(transition = navigationSlideInOut(false))
        },
        onAddNestedPack = viewModel::addNestedPack,
        onRemoveNestedPack = viewModel::removeNestedPack,
        onNext = {
            viewModel.saveSettings()
            navController.navigate(IconPackPreviewScreen)
        }
    )
}

@Composable
@Preview
private fun IconPackModeSetupUI(
    state: IconPackModeState,
    onValueChange: (InputChange) -> Unit,
    onAddNestedPack: () -> Unit,
    onRemoveNestedPack: (NestedPack) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        TopAppBar {
            BackAction(onBack)
            AppBarTitle("IconPack mode setup")
        }
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val iconPackName = state.inputFieldState.iconPackName
            val packageName = state.inputFieldState.packageName

            InputField(
                modifier = Modifier.fillMaxWidth(),
                caption = "Package",
                value = packageName.text,
                isError = packageName.validationResult is ValidationResult.Error,
                tooltipValue = getPackageAnnotatedString(packageName.text, iconPackName.text),
                onValueChange = {
                    onValueChange(InputChange.PackageName(it))
                },
                supportingText = if (packageName.validationResult is ValidationResult.Error) {
                    {
                        Text(
                            text = when (packageName.validationResult.errorCriteria) {
                                ErrorCriteria.EMPTY -> "Value can't be empty"
                                ErrorCriteria.INCONSISTENT_FORMAT -> "Invalid package"
                                ErrorCriteria.FIRST_LETTER_LOWER_CASE -> error("not possible")
                            }
                        )
                    }
                } else {
                    null
                }
            )
            VerticalSpacer(32.dp)

            InputField(
                modifier = Modifier.fillMaxWidth(),
                caption = "Icon pack name",
                value = iconPackName.text,
                tooltipValue = getIconPackAnnotatedString(iconPackName.text),
                isError = iconPackName.validationResult is ValidationResult.Error,
                onValueChange = {
                    onValueChange(IconPackName(it))
                },
                supportingText = if (iconPackName.validationResult is ValidationResult.Error) {
                    {
                        Text(
                            text = when (iconPackName.validationResult.errorCriteria) {
                                ErrorCriteria.EMPTY -> "Value can't be empty"
                                ErrorCriteria.INCONSISTENT_FORMAT -> "Invalid name"
                                ErrorCriteria.FIRST_LETTER_LOWER_CASE -> "First letter should be uppercase"
                            }
                        )
                    }
                } else {
                    null
                }
            )
            if (state.nestedPacks.isEmpty()) {
                TextButton(
                    modifier = Modifier.align(Alignment.Start),
                    onClick = onAddNestedPack
                ) {
                    Text(text = "+ Add nested pack")
                }
            } else {
                NestedPacks(
                    nestedPacks = state.nestedPacks,
                    onRemove = onRemoveNestedPack,
                    onValueChange = onValueChange,
                    onAddNestedPack = onAddNestedPack
                )
            }

            VerticalSpacer(36.dp)
            Button(
                modifier = Modifier.align(Alignment.End),
                enabled = state.nextAvailable,
                onClick = onNext,
            ) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
private fun NestedPacks(
    nestedPacks: List<NestedPack>,
    onRemove: (NestedPack) -> Unit,
    onAddNestedPack: () -> Unit,
    onValueChange: (InputChange) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        VerticalSpacer(8.dp)
        nestedPacks.forEachIndexed { index, nestedPack ->
            val inputFieldState = nestedPack.inputFieldState

            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier.padding(top = 6.dp),
                    onClick = { onRemove(nestedPack) }
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
                InputTextField(
                    modifier = Modifier.weight(1f),
                    value = inputFieldState.text,
                    isError = inputFieldState.validationResult is ValidationResult.Error,
                    onValueChange = {
                        onValueChange(NestedPackName(id = nestedPack.id, text = it))
                    },
                    supportingText = if (inputFieldState.validationResult is ValidationResult.Error) {
                        {
                            Text(
                                text = when (inputFieldState.validationResult.errorCriteria) {
                                    ErrorCriteria.EMPTY -> "Value can't be empty"
                                    ErrorCriteria.INCONSISTENT_FORMAT -> "Invalid name"
                                    ErrorCriteria.FIRST_LETTER_LOWER_CASE -> "First letter should be uppercase"
                                }
                            )
                        }
                    } else {
                        null
                    }
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
            onClick = onAddNestedPack
        ) {
            Text(text = "+ Add nested pack")
        }
    }
}

@Preview
@Composable
private fun IconPackModeSetupUIPreview() {
    IconPackModeSetupUI(
        state = IconPackModeState(
            nestedPacks = listOf(
                NestedPack(
                    id = "0",
                    inputFieldState = InputState("", ValidationResult.Error(ErrorCriteria.EMPTY))
                )
            )
        ),
        onValueChange = {},
        onBack = {},
        onAddNestedPack = {},
        onRemoveNestedPack = {},
        onNext = {}
    )
}