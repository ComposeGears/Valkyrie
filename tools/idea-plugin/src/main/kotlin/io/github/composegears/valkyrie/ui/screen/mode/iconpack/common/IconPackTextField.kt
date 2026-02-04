package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.textfield.validation.ErrorCriteria
import io.github.composegears.valkyrie.jewel.textfield.validation.ValidationResult
import io.github.composegears.valkyrie.jewel.textfield.validation.ValidationTextField
import io.github.composegears.valkyrie.jewel.textfield.validation.ValidationUseCase
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.InfoText

@Composable
fun IconPackTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValidationChange: (ValidationResult) -> Unit = {},
) {
    ValidationTextField(
        modifier = modifier,
        enabled = enabled,
        text = text,
        validationUseCase = remember { IconPackValidationUseCase() },
        errorMessageMapper = {
            when (it) {
                ErrorCriteria.EMPTY -> "Value can't be empty"
                ErrorCriteria.INCONSISTENT_FORMAT -> "Invalid name"
                ErrorCriteria.FIRST_LETTER_LOWER_CASE -> "First letter should be uppercase"
            }
        },
        onValueChange = onValueChange,
        onValidationChange = onValidationChange,
    )
}

private class IconPackValidationUseCase : ValidationUseCase {

    private val iconPackRegex = "^[A-Za-z0-9]*$".toRegex()

    override suspend fun invoke(params: String): ValidationResult {
        return when {
            params.isEmpty() -> ValidationResult.Error(errorCriteria = ErrorCriteria.EMPTY)
            params.first().isLowerCase() -> ValidationResult.Error(errorCriteria = ErrorCriteria.FIRST_LETTER_LOWER_CASE)
            !params.matches(iconPackRegex) -> ValidationResult.Error(errorCriteria = ErrorCriteria.INCONSISTENT_FORMAT)
            else -> ValidationResult.Success
        }
    }
}

@Preview
@Composable
private fun IconPackTextFieldPreview() = PreviewTheme(alignment = Alignment.Center) {
    var latestValue by rememberMutableState { "" }

    Column {
        IconPackTextField(
            modifier = Modifier.width(200.dp),
            text = "ValkyrieIcons",
            onValueChange = { latestValue = it },
        )
        Spacer(8.dp)
        IconPackTextField(
            modifier = Modifier.width(200.dp),
            text = "ValkyrieIcons",
            enabled = false,
            onValueChange = { latestValue = it },
        )
        Spacer(16.dp)
        InfoText(text = latestValue)
    }
}
