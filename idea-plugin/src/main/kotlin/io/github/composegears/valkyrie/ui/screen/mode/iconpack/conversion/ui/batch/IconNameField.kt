package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.BaseTextField
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun IconNameField(
  value: String,
  modifier: Modifier = Modifier,
  onValueChange: (String) -> Unit,
) {
  val focusManager = LocalFocusManager.current

  var text by rememberMutableState(value) { value }
  var isFocused by rememberMutableState { false }

  val interactionSource = remember { MutableInteractionSource() }

  BaseTextField(
    modifier = modifier
      .fillMaxWidth()
      .height(40.dp)
      .clip(RoundedCornerShape(8.dp))
      .indication(interactionSource, LocalIndication.current)
      .hoverable(interactionSource)
      .onFocusChanged {
        isFocused = it.isFocused
        if (!isFocused) {
          onValueChange(text)
        }
      }
      .onKeyEvent {
        when (it.key) {
          Key.Escape -> {
            focusManager.clearFocus()
            text = value
            true
          }
          Key.Enter -> {
            onValueChange(text)
            focusManager.clearFocus()
            true
          }
          else -> false
        }
      },
    value = text,
    onValueChange = { text = it },
    isError = text.isEmpty() || text.contains(" "),
    placeholder = if (text.isEmpty()) {
      {
        Text(
          text = "Could not be empty",
          color = MaterialTheme.colorScheme.onError,
        )
      }
    } else {
      null
    },
    trailingIcon = if (isFocused) {
      {
        IconButton(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape),
          imageVector = Icons.Default.Check,
          iconSize = 18.dp,
          onClick = {
            onValueChange(text)
            focusManager.clearFocus()
          },
        )
      }
    } else {
      null
    },
  )
}

@Preview
@Composable
private fun IconNameFieldPreview() = PreviewTheme {
  IconNameField(
    modifier = Modifier.width(300.dp),
    value = "IconName",
    onValueChange = {},
  )
}
