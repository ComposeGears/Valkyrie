package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.dashedBorder
import io.github.composegears.valkyrie.ui.foundation.icons.AddFile
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun DragAndDropOverlay(
    isDragging: Boolean,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        when {
            isDragging -> MaterialTheme.colorScheme.background.copy(alpha = 0.97f)
            else -> MaterialTheme.colorScheme.background.copy(alpha = 0.0f)
        },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(32.dp)
            .clip(MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .widthIn(max = 350.dp)
                .aspectRatio(1f),
            visible = isDragging,
        ) {
            Box(
                modifier = Modifier
                    .dashedBorder(
                        strokeWidth = 1.dp,
                        gapWidth = 8.dp,
                        dashWidth = 8.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        shape = MaterialTheme.shapes.small,
                    ),
            ) {
                CenterVerticalRow(modifier = Modifier.align(Alignment.Center)) {
                    Icon(
                        imageVector = ValkyrieIcons.AddFile,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "Add more icons",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DragAndDropOverlayPreview() = PreviewTheme {
    DragAndDropOverlay(isDragging = true)
}
