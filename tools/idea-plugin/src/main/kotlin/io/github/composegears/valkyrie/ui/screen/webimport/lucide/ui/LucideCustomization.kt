package io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.HorizontalSpacer
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import kotlin.math.roundToInt

@Composable
fun LucideCustomization(
    settings: LucideSettings,
    onSettingsChange: (LucideSettings) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CenterVerticalRow {
            IconButton(
                imageVector = ValkyrieIcons.Close,
                onClick = onClose,
            )
            Text(
                text = "Customize Icon",
                style = MaterialTheme.typography.titleMedium,
            )
            WeightSpacer()
            TextButton(
                onClick = {
                    onSettingsChange(LucideSettings())
                },
                enabled = settings.isModified,
            ) {
                Text(text = "Reset")
            }
            HorizontalSpacer(4.dp)
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            VerticalSpacer(8.dp)

            Text(
                text = "Stroke Width: ${String.format("%.1f", settings.strokeWidth)}",
                style = MaterialTheme.typography.labelMedium,
            )
            VerticalSpacer(4.dp)
            Slider(
                value = settings.strokeWidth,
                onValueChange = {
                    onSettingsChange(settings.copy(strokeWidth = it))
                },
                valueRange = 0.5f..4.0f,
                steps = 34,
            )

            VerticalSpacer(16.dp)

            Text(
                text = "Size: ${settings.size}px",
                style = MaterialTheme.typography.labelMedium,
            )
            VerticalSpacer(4.dp)
            Slider(
                value = settings.size.toFloat(),
                onValueChange = {
                    onSettingsChange(settings.copy(size = it.roundToInt()))
                },
                valueRange = 16f..96f,
                steps = 79,
            )

            VerticalSpacer(16.dp)

            CenterVerticalRow {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Absolute Stroke Width",
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Text(
                        text = "Maintain stroke width when scaling",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    )
                }
                HorizontalSpacer(8.dp)
                Switch(
                    checked = settings.absoluteStrokeWidth,
                    onCheckedChange = {
                        onSettingsChange(settings.copy(absoluteStrokeWidth = it))
                    },
                )
            }

            VerticalSpacer(16.dp)
        }
    }
}
