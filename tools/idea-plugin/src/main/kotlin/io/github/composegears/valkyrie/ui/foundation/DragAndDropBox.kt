package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DragAndDropBox(
    isDragging: Boolean,
    onChoose: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    content: @Composable BoxScope.() -> Unit,
) {
    var isHover by rememberMutableState(isDragging) { isDragging }

    val dashColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val border by animateDpAsState(if (isHover) 2.dp else 1.dp)

    Box(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .heightIn(min = 300.dp)
            .onPointerEvent(PointerEventType.Enter) { isHover = true }
            .onPointerEvent(PointerEventType.Exit) { isHover = false }
            .dashedBorder(
                strokeWidth = border,
                gapWidth = 8.dp,
                dashWidth = 8.dp,
                color = dashColor,
                shape = shape,
            )
            .padding(1.dp)
            .background(
                color = when {
                    isHover -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    else -> Color.Transparent
                },
                shape = shape,
            )
            .clip(shape)
            .clickable(
                onClick = onChoose,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

@Preview
@Composable
private fun DragAndDropBoxPreview() = PreviewTheme {
    DragAndDropBox(
        isDragging = false,
        onChoose = {},
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Content")
        }
    }
}
