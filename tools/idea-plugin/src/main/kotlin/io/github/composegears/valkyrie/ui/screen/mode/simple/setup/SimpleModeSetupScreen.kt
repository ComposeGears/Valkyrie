package io.github.composegears.valkyrie.ui.screen.mode.simple.setup

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigationSlideInOut
import com.composegears.tiamat.compose.replace
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.ui.domain.validation.ErrorCriteria
import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.domain.validation.ValidationResult
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.InputField
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.setup.SimpleModeInputChange.PackageName
import io.github.composegears.valkyrie.ui.screen.mode.simple.setup.util.buildPackageHighlight
import kotlinx.coroutines.Dispatchers

val SimpleModeSetupScreen by navDestination<Unit> {
    val navController = navController()
    val viewModel = viewModel<SimpleModeSetupViewModel>()

    val state by viewModel.state.collectAsState(Dispatchers.Main.immediate)

    SimpleModeSetupScreenUI(
        state = state,
        onValueChange = viewModel::onValueChange,
        onBack = {
            navController.back(transition = navigationSlideInOut(false))
        },
        onNext = {
            viewModel.saveSettings()
            navController.replace(
                dest = SimpleConversionScreen,
                transition = navigationSlideInOut(true),
            )
        },
    )
}

@Composable
private fun SimpleModeSetupScreenUI(
    state: SimpleModeSetupState,
    onValueChange: (SimpleModeInputChange) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    Column {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = "Simple mode setup")
        }
        VerticalSpacer(36.dp)
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val packageName = state.packageName
            InputField(
                modifier = Modifier.fillMaxWidth(),
                caption = "Package",
                value = packageName.text,
                isError = packageName.validationResult is ValidationResult.Error,
                highlights = buildPackageHighlight(packageName.text),
                onValueChange = {
                    onValueChange(PackageName(it))
                },
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
            VerticalSpacer(24.dp)
            Button(
                modifier = Modifier
                    .align(Alignment.End),
                enabled = state.nextAvailable,
                onClick = onNext,
            ) {
                Text(text = "Next")
            }
        }
    }
}

@Preview
@Composable
private fun SimpleModeSetupScreenPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    SimpleModeSetupScreenUI(
        state = SimpleModeSetupState(
            packageName = InputState(text = "com.example"),
            nextAvailable = true,
        ),
        onValueChange = {},
        onBack = {},
        onNext = {},
    )
}
