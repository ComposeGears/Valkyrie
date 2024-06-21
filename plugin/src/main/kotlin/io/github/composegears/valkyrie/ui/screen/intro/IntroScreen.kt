package io.github.composegears.valkyrie.ui.screen.intro

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.NavDestination
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.components.InputField
import io.github.composegears.valkyrie.ui.screen.conversion.ConversionScreen
import io.github.composegears.valkyrie.ui.screen.intro.InputChange.IconPackName
import io.github.composegears.valkyrie.ui.screen.intro.util.getIconPackAnnotatedString
import io.github.composegears.valkyrie.ui.screen.intro.util.getPackageAnnotatedString
import kotlinx.coroutines.Dispatchers

val IntroScreen: NavDestination<Unit> by navDestination {

    val navController = navController()
    val introViewModel = koinTiamatViewModel<IntroViewModel>()

    val introState by introViewModel.introState.collectAsState(Dispatchers.Main.immediate)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        IntroScreenUI(
            state = introState,
            onValueChange = introViewModel::onValueChange,
            onNext = {
                introViewModel.saveSettings()
                navController.replace(ConversionScreen)
            }
        )
    }
}

@Composable
@Preview
private fun IntroScreenUI(
    state: IntroState,
    onValueChange: (InputChange) -> Unit,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val iconPackName = state.inputFieldState.iconPackName
        val packageName = state.inputFieldState.packageName

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to Valkyrie",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Customize basic preferences or proceed with default values",
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
            VerticalSpacer(32.dp)

            InputField(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .padding(horizontal = 16.dp),
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
                            }
                        )
                    }
                } else {
                    null
                }
            )

            VerticalSpacer(32.dp)

            InputField(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .padding(horizontal = 16.dp),
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
                            }
                        )
                    }
                } else {
                    null
                }
            )
            VerticalSpacer(36.dp)
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp),
                enabled = state.nextAvailable,
                onClick = onNext,
            ) {
                Text(text = "Next")
            }
        }
    }
}
