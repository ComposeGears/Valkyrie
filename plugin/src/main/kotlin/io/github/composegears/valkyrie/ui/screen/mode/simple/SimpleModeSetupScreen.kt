package io.github.composegears.valkyrie.ui.screen.mode.simple

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.screen.conversion.ConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.SimpleModeInputChange.PackageName
import kotlinx.coroutines.Dispatchers

val SimpleModeSetupScreen by navDestination<Unit> {
    val navController = navController()
    val viewModel = koinTiamatViewModel<SimpleModeSetupViewModel>()

    val state by viewModel.state.collectAsState(Dispatchers.Main.immediate)

    SimpleModeSetupScreenUI(
        state = state,
        onValueChange = viewModel::onValueChange,
        onBack = {
            navController.back(transition = navigationSlideInOut(false))
        },
        onNext = {
            viewModel.saveSettings()
            navController.navigate(
                dest = ConversionScreen,
                transition = navigationSlideInOut(true)
            )
        }
    )
}

@Composable
private fun SimpleModeSetupScreenUI(
    state: SimpleModeSetupState,
    onValueChange: (SimpleModeInputChange) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        TopAppBar {
            BackAction(onBack)
            AppBarTitle("Simple mode setup")
        }
        VerticalSpacer(24.dp)
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Customize basic preferences or proceed with default values",
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
            VerticalSpacer(24.dp)

            val packageName = state.packageName
            InputField(
                modifier = Modifier.fillMaxWidth(),
                caption = "Package",
                value = packageName.text,
                isError = packageName.validationResult is ValidationResult.Error,
                tooltipValue = getPackageHint(packageName.text),
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
                            }
                        )
                    }
                } else {
                    null
                }
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

private fun getPackageHint(packageName: String): AnnotatedString {
    val placeholder = packageName.ifEmpty { "your.package" }
    val codeBlock = """
        package $placeholder
        
        val MyIcon: ImageVector
            get() {
                if (_MyIcon != null) {
                return _MyIcon!!
            }
            ...
       }
       """.trimIndent()

    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.ExtraLight)) {
            append(codeBlock)
        }
        val startIndex = codeBlock.indexOf(placeholder)
        val lastIndex = startIndex + placeholder.length
        addStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            ),
            start = startIndex,
            end = lastIndex
        )
    }
}

@Preview
@Composable
private fun SimpleModeSetupScreenPreview() {
    SimpleModeSetupScreenUI(
        state = SimpleModeSetupState(
            packageName = InputState(text = "com.example"),
            nextAvailable = true
        ),
        onValueChange = {},
        onBack = {},
        onNext = {}
    )
}