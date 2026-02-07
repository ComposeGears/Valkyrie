package io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.NoStopIndicatorSlider
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import kotlin.math.roundToInt

@Composable
fun LucideCustomization(
    settings: LucideSettings,
    onSettingsChange: (LucideSettings) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var strokeWidth by remember { mutableFloatStateOf(settings.strokeWidth) }
    var size by remember { mutableIntStateOf(settings.size) }

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
                    strokeWidth = LucideSettings.DEFAULT_STROKE_WIDTH
                    size = LucideSettings.DEFAULT_SIZE
                    onSettingsChange(LucideSettings())
                },
                enabled = settings.isModified,
            ) {
                Text(text = "Reset")
            }
            Spacer(4.dp)
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(8.dp)

            Text(
                text = "Stroke width: ${String.format("%.1f", strokeWidth)}",
                fontWeight = FontWeight.Medium,
            )
            Spacer(4.dp)
            NoStopIndicatorSlider(
                value = strokeWidth,
                onValueChange = { strokeWidth = it },
                onValueChangeFinished = {
                    onSettingsChange(settings.copy(strokeWidth = strokeWidth))
                },
                valueRange = 0.5f..4.0f,
                steps = 34,
            )

            Spacer(16.dp)

            Text(
                text = "Size: ${size}px",
                fontWeight = FontWeight.Medium,
            )
            Spacer(4.dp)
            NoStopIndicatorSlider(
                value = size.toFloat(),
                onValueChange = { size = it.roundToInt() },
                onValueChangeFinished = {
                    onSettingsChange(settings.copy(size = size))
                },
                valueRange = 16f..48f,
                steps = 31,
            )

            Spacer(16.dp)

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
                Spacer(8.dp)
                Switch(
                    checked = settings.absoluteStrokeWidth,
                    onCheckedChange = {
                        onSettingsChange(settings.copy(absoluteStrokeWidth = it))
                    },
                )
            }

            Spacer(16.dp)
        }
    }
}
