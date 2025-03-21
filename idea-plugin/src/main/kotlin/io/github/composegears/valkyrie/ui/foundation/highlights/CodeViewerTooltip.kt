package io.github.composegears.valkyrie.ui.foundation.highlights

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.snipme.highlights.Highlights
import io.github.composegears.valkyrie.ui.foundation.disabled
import io.github.composegears.valkyrie.ui.foundation.highlights.core.CodeViewer
import io.github.composegears.valkyrie.ui.foundation.icons.Help
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeViewerTooltip(
    highlights: Highlights,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TooltipArea(
        modifier = modifier,
        tooltip = {
            Surface(
                tonalElevation = 0.dp,
                shadowElevation = 4.dp,
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                if (enabled) {
                    CodeViewer(
                        modifier = Modifier.padding(8.dp),
                        highlights = highlights,
                    )
                }
            }
        },
        content = {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = ValkyrieIcons.Help,
                tint = when {
                    enabled -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurface.disabled()
                },
                contentDescription = null,
            )
        },
    )
}
