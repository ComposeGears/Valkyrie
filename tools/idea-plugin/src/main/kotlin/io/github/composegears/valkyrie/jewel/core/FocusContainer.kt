package io.github.composegears.valkyrie.jewel.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.theme.iconButtonStyle

@Composable
fun FocusContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable BoxScope.() -> Unit,
) {
    val isHover by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .border(
                width = JewelTheme.globalMetrics.outlineWidth,
                color = JewelTheme.globalColors.borders.normal,
                shape = RoundedCornerShape(12.dp),
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isPressed -> JewelTheme.iconButtonStyle.colors.backgroundPressed
                    isHover -> JewelTheme.iconButtonStyle.colors.backgroundHovered
                    else -> JewelTheme.iconButtonStyle.colors.background
                },
            )
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null,
            ),
        content = content
    )
}

@Preview
@Composable
private fun FocusContainerPreview() = PreviewTheme(alignment = Alignment.Center) {
    FocusContainer(onClick = {}) {
        Text(
            text = "Focusable",
            modifier = Modifier.padding(16.dp)
        )
    }
}