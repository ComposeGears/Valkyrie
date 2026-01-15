package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.filled.Help
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun TooltipIcon(
    hint: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    TooltipButton(
        modifier = modifier,
        delayMillis = 0,
        hint = hint,
    ) {
        Box(
            modifier = Modifier.size(24.dp),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipButton(
    hint: String,
    modifier: Modifier = Modifier,
    delayMillis: Int = 1000,
    content: @Composable () -> Unit,
) {
    TooltipArea(
        modifier = modifier,
        delayMillis = delayMillis,
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
        TooltipIcon(
            hint = "Show black background",
            icon = ValkyrieIcons.Filled.Help,
        )
        TooltipButton(hint = "Test") {
            Text(
                text = "Generic content",
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
