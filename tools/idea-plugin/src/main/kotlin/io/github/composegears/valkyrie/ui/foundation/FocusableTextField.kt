package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.util.dim
import io.github.composegears.valkyrie.compose.util.disabled
import io.github.composegears.valkyrie.ui.foundation.Mode.Edit
import io.github.composegears.valkyrie.ui.foundation.Mode.View
import io.github.composegears.valkyrie.ui.foundation.icons.Checked
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.foundation.icons.Edit
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

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

    var textFieldValue by rememberMutableState(value) {
        TextFieldValue(
            text = value,
            selection = TextRange(value.length),
        )
    }

    var isHover by rememberMutableState { false }
    var mode by rememberMutableState { View }
    val hoverAlpha by animateFloatAsState(if (isHover && mode == View) 1f else 0f)

    val isError by remember(textFieldValue) {
        derivedStateOf { textFieldValue.text.isEmpty() || textFieldValue.text.contains(" ") }
    }

    CenterVerticalRow(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { isHover = true }
            .onPointerEvent(PointerEventType.Exit) { isHover = false }
            .border(
                width = Dp.Hairline,
                color = when (mode) {
                    Edit -> {
                        when {
                            isError -> colors.errorColor
                            else -> colors.focusedBorderColor
                        }
                    }
                    else -> colors.unfocusedBorderColor
                },
                shape = shape,
            )
            .clip(shape)
            .focusProperties { canFocus = false },
    ) {
        BasicTextField(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = { mode = Edit })
                }
                .focusProperties { canFocus = true }
                .focusRequester(focusRequester)
                .focusable()
                .weight(1f)
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
                    if (isError && mode == View) {
                        Text(
                            text = "icon name required",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.errorColor,
                        )
                    }
                }
            },
        )

        AnimatedContent(
            modifier = Modifier.height(32.dp),
            targetState = mode,
        ) { targetMode ->
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
                    CenterVerticalRow(
                        modifier = Modifier.padding(end = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        FilledIconButton(
                            modifier = Modifier.size(20.dp),
                            colors = IconButtonDefaults.filledIconButtonColors()
                                .copy(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            onClick = {
                                focusRequester.freeFocus()
                                mode = View
                                textFieldValue = textFieldValue.copy(text = value)
                            },
                        ) {
                            Icon(
                                modifier = Modifier.size(12.dp),
                                imageVector = ValkyrieIcons.Close,
                                contentDescription = null,
                            )
                        }
                        FilledIconButton(
                            modifier = Modifier.size(20.dp),
                            colors = IconButtonDefaults.filledIconButtonColors()
                                .copy(containerColor = MaterialTheme.colorScheme.primary.disabled()),
                            enabled = !isError,
                            onClick = {
                                focusRequester.freeFocus()
                                mode = View
                                onValueChange(textFieldValue.text)
                            },
                        ) {
                            Icon(
                                modifier = Modifier.size(12.dp),
                                imageVector = ValkyrieIcons.Checked,
                                contentDescription = null,
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
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
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

@Preview
@Composable
private fun FocusableTextFieldPreview() = PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FocusableTextField(
            modifier = Modifier.width(300.dp),
            value = "IconName",
            onValueChange = {},
        )
        FocusableTextField(
            modifier = Modifier.width(150.dp),
            value = "IconName",
            onValueChange = {},
        )
    }
}
