package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.icons.Backspace
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun InputField(
    caption: String,
    value: String,
    tooltipValue: AnnotatedString,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = caption,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )

            Tooltip(text = tooltipValue)
        }
        InputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            supportingText = supportingText,
        )
    }
}

@Composable
fun InputTextField(
    value: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
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
            unfocusedIndicatorColor = Color.Transparent,
        ),
        isError = isError,
        singleLine = true,
        supportingText = supportingText,
        trailingIcon = {
            if (value.isNotEmpty()) {
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        InputTextField(
            modifier = Modifier.width(300.dp),
            value = "Hello, World!",
            onValueChange = {},
        )
        InputTextField(
            modifier = Modifier.width(300.dp),
            value = "Hello, World!",
            isError = true,
            onValueChange = {},
            supportingText = {
                Text("This is an error message")
            },
        )
    }
}
