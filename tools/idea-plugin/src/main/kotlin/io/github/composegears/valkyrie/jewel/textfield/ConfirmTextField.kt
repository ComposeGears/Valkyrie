package io.github.composegears.valkyrie.jewel.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.IconActionButton
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun ConfirmTextField(
    text: String,
    errorPlaceholder: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    val currentText by rememberUpdatedState(text)
    val state = rememberTextFieldState(text)
    val focusManager = LocalFocusManager.current

    val isError by remember { derivedStateOf { state.text.isEmpty() } }
    var focused by rememberMutableState { false }

    TextField(
        modifier = modifier
            .heightIn(min = 32.dp)
            .onFocusChanged {
                if (focused && !it.isFocused && !isError) {
                    // Lost focus - commit the changes if valid
                    val newText = state.text.toString()
                    if (newText != currentText) {
                        onValueChange(newText)
                    }
                }
                focused = it.isFocused
            }
            .onKeyEvent {
                when (it.key) {
                    Key.Escape -> {
                        state.setTextAndPlaceCursorAtEnd(currentText)
                        focusManager.clearFocus()
                        true
                    }
                    Key.Enter -> {
                        if (!isError) {
                            focusManager.clearFocus()
                            onValueChange(state.text.toString())
                            true
                        } else {
                            false
                        }
                    }
                    else -> false
                }
            },
        state = state,
        outline = when {
            isError -> Outline.Error
            else -> Outline.None
        },
        placeholder = if (isError) {
            {
                Text(text = errorPlaceholder)
            }
        } else {
            null
        },
        trailingIcon = {
            if (focused && !isError) {
                IconActionButton(
                    key = AllIconsKeys.Actions.Checked,
                    contentDescription = null,
                    onClick = {
                        onValueChange(state.text.toString())
                    },
                )
            }
        },
    )
}

@Preview
@Composable
private fun ConfirmTextFieldPreview() = PreviewTheme(alignment = Alignment.Center) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        var text by rememberMutableState { "IconName" }

        ConfirmTextField(
            modifier = Modifier.width(200.dp),
            text = text,
            errorPlaceholder = "Can't be empty",
            onValueChange = { text = it },
        )
        InfoText(text = "Initial value: $text")
    }
}
