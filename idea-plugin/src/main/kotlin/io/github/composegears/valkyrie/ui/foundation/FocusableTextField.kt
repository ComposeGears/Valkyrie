package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.Mode.Edit
import io.github.composegears.valkyrie.ui.foundation.Mode.View
import io.github.composegears.valkyrie.ui.foundation.icons.Checked
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.foundation.icons.Edit
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FocusableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    colors: FocusableTextFieldColor = FocusableTextFieldDefaults.colors(),
) {
    val focusRequester = remember { FocusRequester() }

    var textFieldValue by rememberMutableState {
        TextFieldValue(
            text = value,
            selection = TextRange(value.length),
        )
    }

    var isHover by rememberMutableState { false }
    var mode by rememberMutableState { View }
    val hoverAlpha by animateFloatAsState(if (isHover && mode == View) 1f else 0f)

    val isError by remember {
        derivedStateOf { textFieldValue.text.isEmpty() || textFieldValue.text.contains(" ") }
    }

    Row(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { isHover = true }
            .onPointerEvent(PointerEventType.Exit) { isHover = false }
            .border(
                width = Dp.Hairline,
                color = if (mode == Edit) colors.focusedBorderColor else colors.unfocusedBorderColor,
                shape = shape,
            )
            .clip(shape)
            .focusProperties { canFocus = false },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { mode = Edit })
                }
                .focusProperties { canFocus = true }
                .focusRequester(focusRequester)
                .focusable()
                .widthIn(max = 150.dp)
                .height(32.dp)
                .onKeyEvent {
                    when (it.key) {
                        Key.Escape -> {
                            focusRequester.freeFocus()
                            mode = View
                            textFieldValue = TextFieldValue(text = value)
                            true
                        }
                        Key.Enter -> {
                            if (!isError) {
                                focusRequester.freeFocus()
                                mode = View
                                onValueChange(textFieldValue.text)
                                true
                            } else {
                                false
                            }
                        }
                        else -> false
                    }
                },
            singleLine = true,
            value = textFieldValue,
            enabled = mode == Edit,
            readOnly = mode == View,
            onValueChange = { textFieldValue = it },
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = when (mode) {
                    Edit -> when {
                        isError -> colors.errorColor
                        else -> colors.focusedTextColor
                    }
                    View -> colors.unfocusedTextColor
                },
            ),
            cursorBrush = SolidColor(colors.cursorColor),
            decorationBox = @Composable { innerTextField ->
                Box(
                    modifier = Modifier.padding(start = 12.dp, end = 4.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    innerTextField()
                }
            },
        )

        AnimatedContent(targetState = mode) { targetMode ->
            when (targetMode) {
                View -> {
                    Icon(
                        imageVector = ValkyrieIcons.Edit,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(shape)
                            .clickable {
                                focusRequester.requestFocus()
                                mode = Edit
                            }
                            .padding(4.dp)
                            .graphicsLayer { alpha = hoverAlpha },
                    )
                }
                Edit -> {
                    Row {
                        Icon(
                            imageVector = ValkyrieIcons.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(shape)
                                .clickable {
                                    focusRequester.freeFocus()
                                    mode = View
                                    textFieldValue = textFieldValue.copy(text = value)
                                }
                                .padding(4.dp),
                        )
                        Icon(
                            imageVector = ValkyrieIcons.Checked,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(shape)
                                .clickable(enabled = !isError) {
                                    focusRequester.freeFocus()
                                    mode = View
                                    onValueChange(textFieldValue.text)
                                }
                                .padding(4.dp),
                            tint = if (isError) {
                                LocalContentColor.current.disabled()
                            } else {
                                LocalContentColor.current
                            },
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(mode) {
        if (mode == Edit) {
            focusRequester.requestFocus()
        }
    }
}

object FocusableTextFieldDefaults {
    @Composable
    fun colors(): FocusableTextFieldColor {
        return FocusableTextFieldColor(
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.dim(),
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
            unfocusedBorderColor = Color.Transparent,
            errorColor = MaterialTheme.colorScheme.error,
        )
    }
}

data class FocusableTextFieldColor(
    val focusedTextColor: Color,
    val unfocusedTextColor: Color,
    val cursorColor: Color,
    val focusedBorderColor: Color,
    val unfocusedBorderColor: Color,
    val errorColor: Color,
)

private enum class Mode {
    Edit,
    View,
}
