package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
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
fun PackageTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onValidationChange: (ValidationResult) -> Unit = {},
) {
    ValidationTextField(
        modifier = modifier,
        state = rememberTextFieldState(text),
        validationUseCase = remember { PackageValidationUseCase() },
        errorMessageMapper = {
            when (it) {
                ErrorCriteria.EMPTY -> "Value can't be empty"
                ErrorCriteria.INCONSISTENT_FORMAT -> "Invalid package"
                else -> error("not possible")
            }
        },
        onValueChange = onValueChange,
        onValidationChange = onValidationChange,
    )
}

private class PackageValidationUseCase : ValidationUseCase {

    private val packageRegex = "^([A-Za-z][A-Za-z\\d_]*\\.)*[A-Za-z][A-Za-z\\d_]*$".toRegex()

    override suspend fun invoke(params: String): ValidationResult {
        return when {
            params.isEmpty() -> ValidationResult.Error(errorCriteria = ErrorCriteria.EMPTY)
            !params.matches(packageRegex) -> ValidationResult.Error(errorCriteria = ErrorCriteria.INCONSISTENT_FORMAT)
            else -> ValidationResult.Success
        }
    }
}

@Preview
@Composable
private fun PackageTextFieldPreview() = PreviewTheme(alignment = Alignment.Center) {
    var latestValue by rememberMutableState { "" }

    Column {
        PackageTextField(
            modifier = Modifier.width(200.dp),
            text = "com.example.iconpack",
            onValueChange = { latestValue = it },
        )
        Spacer(8.dp)
        InfoText(text = latestValue)
    }
}
