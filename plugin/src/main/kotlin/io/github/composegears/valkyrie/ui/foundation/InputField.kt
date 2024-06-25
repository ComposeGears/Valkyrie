package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
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
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedTrailingIconColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            cursorColor = MaterialTheme.colorScheme.surface,
            errorTextColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            errorCursorColor = MaterialTheme.colorScheme.surface,
            errorTrailingIconColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            errorIndicatorColor = Color.Transparent,
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
                        modifier = Modifier.size(18.dp),
                        imageVector = ValkyrieIcons.Backspace,
                        contentDescription = null
                    )
                }
            }
        }
    )
}