package io.github.composegears.valkyrie.jewel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.ui.foundation.icons.BatchProcessing
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.theme.iconButtonStyle
import org.jetbrains.jewel.ui.typography

@Composable
fun InfoCard(
    onClick: () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    image: ImageVector? = null,
    icon: ImageVector? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
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
    ) {
        Column {
            CenterVerticalRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                icon?.let {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Top)
                            .size(36.dp),
                        imageVector = it,
                        contentDescription = null,
                        tint = JewelTheme.contentColor,
                    )
                }
                image?.let {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Top)
                            .size(36.dp),
                        imageVector = it,
                        contentDescription = null,
                    )
                }
                Column(
                    modifier = Modifier.width(250.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = title,
                        style = JewelTheme.typography.h4TextStyle,
                        maxLines = 1,
                    )
                    InfoText(
                        text = description,
                        maxLines = 2,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun InfoCardPreview() = PreviewTheme(alignment = Alignment.Center) {
    InfoCard(
        onClick = {},
        icon = ValkyrieIcons.BatchProcessing,
        title = "Title",
        description = "Long description",
    )
}
