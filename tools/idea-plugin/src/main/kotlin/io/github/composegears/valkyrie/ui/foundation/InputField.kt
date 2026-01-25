package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.Backspace
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun InputTextField(
    value: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        enabled = enabled,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorTextColor = MaterialTheme.colorScheme.error,
            errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            errorCursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        isError = isError,
        singleLine = true,
        supportingText = supportingText,
        trailingIcon = {
            if (value.isNotEmpty() && enabled) {
                IconButton(
                    imageVector = ValkyrieIcons.Backspace,
                    iconSize = 18.dp,
                    onClick = { onValueChange("") },
                )
            }
        },
    )
}

@Preview
@Composable
private fun InputTextFieldPreview() = PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        InputTextField(
            modifier = Modifier.width(300.dp),
            value = "Default state",
            onValueChange = {},
        )
        InputTextField(
            modifier = Modifier.width(300.dp),
            value = "Error state",
            isError = true,
            onValueChange = {},
            supportingText = {
                Text("This is an error message")
            },
        )
        InputTextField(
            modifier = Modifier.width(300.dp),
            value = "Disabled state",
            enabled = false,
            isError = false,
            onValueChange = {},
        )
    }
}
