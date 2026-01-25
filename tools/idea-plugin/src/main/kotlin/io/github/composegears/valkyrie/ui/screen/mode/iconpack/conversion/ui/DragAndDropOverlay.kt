package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.colors.softContentColor
import io.github.composegears.valkyrie.jewel.graphics.dashedBorder
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun DragAndDropOverlay(
    isDragging: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    val backgroundColor by animateColorAsState(
        when {
            isDragging -> JewelTheme.globalColors.borders.normal.copy(alpha = 0.97f)
            else -> JewelTheme.globalColors.borders.normal.copy(alpha = 0.0f)
        },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(32.dp)
            .clip(shape),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .widthIn(max = 350.dp)
                .aspectRatio(1f),
            visible = isDragging,
        ) {
            val dashColor = JewelTheme.softContentColor

            Box(
                modifier = Modifier
                    .dashedBorder(
                        strokeWidth = 2.dp,
                        gapWidth = 8.dp,
                        dashWidth = 8.dp,
                        color = dashColor,
                        shape = shape,
                    ),
            ) {
                CenterVerticalRow(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        key = AllIconsKeys.Actions.AddFile,
                        contentDescription = null,
                    )
                    Text(
                        text = stringResource("iconpack.conversion.dnd.add.more"),
                        maxLines = 2,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DragAndDropOverlayPreview() = PreviewTheme(alignment = Alignment.Center) {
    DragAndDropOverlay(isDragging = true)
}
