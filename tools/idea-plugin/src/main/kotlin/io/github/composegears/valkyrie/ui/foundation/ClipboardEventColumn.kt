package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import io.github.composegears.valkyrie.jewel.platform.ClipboardDataType
import io.github.composegears.valkyrie.jewel.platform.pasteFromClipboard

@Composable
fun ClipboardEventColumn(
    onPaste: (ClipboardDataType) -> Unit,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
    FocusableColumn(
        modifier = modifier
            .onPasteEvent {
                pasteFromClipboard()?.let(onPaste)
            },
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FocusableColumn(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusProperties { onExit = { focusRequester.freeFocus() } }
            .focusable()
            .onPointerEvent(PointerEventType.Enter) {
                focusRequester.requestFocus()
            }
            .onPointerEvent(PointerEventType.Exit) {
                focusRequester.freeFocus()
            },
        content = content,
        horizontalAlignment = horizontalAlignment,
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

private fun Modifier.onPasteEvent(callback: () -> Unit): Modifier {
    return this
        .onPreviewKeyEvent { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.isCtrlV()) {
                callback()
                true
            } else {
                false
            }
        }
}

private fun KeyEvent.isCtrlV(): Boolean = (isCtrlPressed || isMetaPressed) && key == Key.V
