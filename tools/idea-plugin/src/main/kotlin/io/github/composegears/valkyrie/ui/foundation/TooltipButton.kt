package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.BlackCircle
import io.github.composegears.valkyrie.ui.foundation.icons.Chessboard
import io.github.composegears.valkyrie.ui.foundation.icons.WhiteCircle
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun TooltipIconButton(
    hint: String,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    TooltipButton(
        modifier = modifier,
        hint = hint,
    ) {
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
    }
}

@Composable
fun TooltipImageButton(
    hint: String,
    image: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TooltipButton(
        modifier = modifier,
        hint = hint,
    ) {
        Box(
            modifier = Modifier.size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                imageVector = image,
                contentDescription = null,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipButton(
    hint: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    TooltipArea(
        modifier = modifier,
        delayMillis = 1_000,
        tooltip = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    text = hint,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        content = content,
    )
}

@Preview
@Composable
private fun TooltipButtonPreview() = PreviewTheme {
    CenterVerticalRow {
        TooltipIconButton(
            hint = "Show pixel grid",
            onClick = {},
            icon = ValkyrieIcons.Chessboard,
        )
        TooltipImageButton(
            hint = "Show white background",
            image = ValkyrieIcons.WhiteCircle,
            onClick = {},
        )
        TooltipImageButton(
            hint = "Show black background",
            image = ValkyrieIcons.BlackCircle,
            onClick = {},
        )
        TooltipButton(hint = "Test") {
            Text(
                text = "Generic content",
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
