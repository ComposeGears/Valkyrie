package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.icons.Help
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tooltip(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
                tonalElevation = 0.dp,
                text = {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = text,
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                colors = TooltipDefaults.richTooltipColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        state = rememberTooltipState(isPersistent = true),
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = ValkyrieIcons.Help,
            tint = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.disabled()
            },
            contentDescription = null,
        )
    }
}
