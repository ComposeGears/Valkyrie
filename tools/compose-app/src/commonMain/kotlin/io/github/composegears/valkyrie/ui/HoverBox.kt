package io.github.composegears.valkyrie.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import io.github.composegears.valkyrie.compose.core.rememberMutableState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HoverBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    var showBorder by rememberMutableState { false }

    DragAndDropBox(
        modifier = modifier
            .onPointerEvent(PointerEventType.Companion.Enter) { showBorder = true }
            .onPointerEvent(PointerEventType.Companion.Exit) { showBorder = false },
        showBorder = showBorder,
        content = content,
    )
}
