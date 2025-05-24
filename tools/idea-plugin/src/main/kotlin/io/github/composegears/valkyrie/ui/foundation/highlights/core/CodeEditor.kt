package io.github.composegears.valkyrie.ui.foundation.highlights.core

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.snipme.highlights.Highlights
import io.github.composegears.valkyrie.compose.core.rememberMutableIntState
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.HorizontalScrollbar
import io.github.composegears.valkyrie.ui.foundation.VerticalScrollbar
import io.github.composegears.valkyrie.ui.foundation.disabled

@Composable
fun CodeEditor(
    highlights: Highlights,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    translateTabToSpaces: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    var currentText by rememberMutableState { TextFieldValue(annotatedString = highlights.buildAnnotatedString()) }
    val linesCount by rememberMutableIntState(currentText) { currentText.annotatedString.count { it == '\n' } + 1 }

    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val linesTextScroll = rememberScrollState()

    LaunchedEffect(linesTextScroll.value) {
        verticalScrollState.scrollTo(linesTextScroll.value)
    }
    LaunchedEffect(verticalScrollState.value) {
        linesTextScroll.scrollTo(verticalScrollState.value)
    }

    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(12.dp * linesCount.toString().length)
                    .verticalScroll(linesTextScroll)
                    .padding(vertical = 16.dp),
                value = IntRange(1, linesCount).joinToString(separator = "\n"),
                readOnly = true,
                textStyle = textStyle.copy(
                    color = colors.focusedTextColor.disabled(),
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp,
                    textAlign = TextAlign.End,
                ),
                onValueChange = {},
            )

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp),
                color = colors.focusedTextColor.copy(alpha = 0.1f),
            )
            Box(modifier = Modifier.fillMaxSize()) {
                TextField(
                    modifier = Modifier
                        .matchParentSize()
                        .horizontalScroll(horizontalScrollState)
                        .verticalScroll(verticalScrollState),
                    onValueChange = { fieldValue ->
                        val fieldUpdate = fieldValue.calculateFieldPhraseUpdate(translateTabToSpaces)
                        currentText = fieldUpdate

                        onValueChange(fieldUpdate.text)
                    },
                    value = TextFieldValue(
                        selection = currentText.selection,
                        composition = currentText.composition,
                        annotatedString = highlights.buildAnnotatedString(),
                    ),
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = textStyle.copy(fontFamily = FontFamily.Monospace),
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    isError = isError,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    shape = shape,
                    colors = colors,
                )

                VerticalScrollbar(adapter = rememberScrollbarAdapter(verticalScrollState))
                HorizontalScrollbar(adapter = rememberScrollbarAdapter(horizontalScrollState))
            }
        }
    }
}

private const val TAB_LENGTH = 4
private const val TAB_CHAR = "\t"

private fun TextFieldValue.calculateFieldPhraseUpdate(translateTabToSpaces: Boolean): TextFieldValue {
    return if (translateTabToSpaces && text.contains(TAB_CHAR)) {
        copy(
            text = text.replace(TAB_CHAR, " ".repeat(TAB_LENGTH)),
            selection = TextRange(index = selection.start + TAB_LENGTH - 1),
        )
    } else {
        this
    }
}
