package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.icons.BlackCircle
import io.github.composegears.valkyrie.ui.foundation.icons.Chessboard
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.WhiteCircle
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipIconButton(
    tooltipText: String,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    TooltipArea(
        modifier = modifier,
        tooltip = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .padding(8.dp),
            ) {
                Text(
                    text = tooltipText,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        content = {
            Box(
                modifier = Modifier.size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onSurface,
                    imageVector = icon,
                    contentDescription = null,
                )
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipImageButton(
    tooltipText: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TooltipArea(
        modifier = modifier,
        tooltip = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .padding(8.dp),
            ) {
                Text(
                    text = tooltipText,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        content = {
            Box(
                modifier = Modifier.size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = null,
                )
            }
        },
    )
}

@Preview
@Composable
private fun TooltipButtonPreview() = PreviewTheme {
    Row {
        TooltipIconButton(
            tooltipText = "Show pixel grid",
            onClick = {},
            icon = ValkyrieIcons.Chessboard,
        )
        TooltipImageButton(
            tooltipText = "Show white background",
            icon = ValkyrieIcons.WhiteCircle,
            onClick = {},
        )
        TooltipImageButton(
            tooltipText = "Show black background",
            icon = ValkyrieIcons.BlackCircle,
            onClick = {},
        )
    }
}
