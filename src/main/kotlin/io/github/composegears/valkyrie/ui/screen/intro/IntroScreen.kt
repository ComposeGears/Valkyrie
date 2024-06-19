package io.github.composegears.valkyrie.ui.screen.intro

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.NavDestination
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.icons.Help
import io.github.composegears.valkyrie.ui.icons.ValkyrieIcons
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
                modifier = Modifier.widthIn(max = 420.dp),
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
                modifier = Modifier.widthIn(max = 420.dp),
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
                modifier = Modifier.align(Alignment.End),
                enabled = state.nextAvailable,
                onClick = onNext,
            ) {
                Text(text = "Next")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    caption: String,
    value: String,
    isError: Boolean = false,
    tooltipValue: AnnotatedString,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = caption,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
            TooltipBox(
                positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                tooltip = {
                    Surface(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Box(modifier = Modifier.padding(PaddingValues(8.dp, 4.dp))) {
                            Text(text = tooltipValue, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                state = rememberTooltipState(isPersistent = true)
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = ValkyrieIcons.Help,
                    contentDescription = null,
                )
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            isError = isError,
            singleLine = true,
            supportingText = supportingText,
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}
