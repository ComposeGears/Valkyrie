package io.github.composegears.valkyrie.jewel.textfield.validation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.colors.errorFocused
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField

@Composable
fun ValidationTextField(
    state: TextFieldState,
    validationUseCase: ValidationUseCase,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    onValidationChange: (ValidationResult) -> Unit = {},
    errorMessageMapper: (ErrorCriteria) -> String,
    modifier: Modifier = Modifier,
) {
    val onValueChangeRemembered by rememberUpdatedState(onValueChange)
    val onValidationChangeRemembered by rememberUpdatedState(onValidationChange)

    var validationResult by rememberMutableState<ValidationResult> { ValidationResult.None }
    var isFirstEmission by rememberMutableState { true }

    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }
            .collectLatest { text ->
                onValueChangeRemembered(text)

                if (isFirstEmission) {
                    isFirstEmission = false
                    return@collectLatest
                }

                delay(200)
                val result = validationUseCase(text)
                validationResult = result
                onValidationChangeRemembered(result)
            }
    }
    ValidationTextField(
        state = state,
        enabled = enabled,
        validationResult = validationResult,
        errorMessageMapper = errorMessageMapper,
        modifier = modifier,
    )
}

@Composable
private fun ValidationTextField(
    state: TextFieldState,
    enabled: Boolean,
    validationResult: ValidationResult,
    errorMessageMapper: (ErrorCriteria) -> String,
    modifier: Modifier = Modifier,
) {
    val isError = validationResult is ValidationResult.Error
    val outline = when {
        isError -> Outline.Error
        else -> Outline.None
    }

    Column(
        modifier = modifier.width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            enabled = enabled,
            outline = outline,
        )
        if (isError) {
            Text(
                text = errorMessageMapper(validationResult.errorCriteria),
                color = JewelTheme.errorFocused,
            )
        }
    }
}

@Preview
@Composable
private fun ValidatedTextFieldPreview() = PreviewTheme(alignment = Alignment.Center) {
    class SampleValidationUseCase : ValidationUseCase {
        override suspend fun invoke(params: String): ValidationResult {
            return when {
                params.isEmpty() -> ValidationResult.Error(ErrorCriteria.EMPTY)
                params.length < 3 -> ValidationResult.Error(ErrorCriteria.INCONSISTENT_FORMAT)
                params.first().isLowerCase() -> ValidationResult.Error(ErrorCriteria.FIRST_LETTER_LOWER_CASE)
                else -> ValidationResult.Success
            }
        }
    }

    val useCase = remember { SampleValidationUseCase() }
    val mapper = { criteria: ErrorCriteria ->
        when (criteria) {
            ErrorCriteria.EMPTY -> "Field cannot be empty"
            ErrorCriteria.INCONSISTENT_FORMAT -> "Length must be at least 3 characters"
            ErrorCriteria.FIRST_LETTER_LOWER_CASE -> "First letter must be uppercase"
        }
    }

    val state = rememberTextFieldState("Text")

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ValidationTextField(
            state = state,
            validationUseCase = useCase,
            errorMessageMapper = mapper,
            enabled = true,
            onValueChange = { },
        )
        ValidationTextField(
            state = state,
            validationUseCase = useCase,
            enabled = false,
            onValueChange = { },
            errorMessageMapper = mapper,
        )
    }
}
