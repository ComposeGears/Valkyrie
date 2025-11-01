package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.HorizontalSpacer
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.filled.Help
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.IconButtonSmall
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.rememberBrowser
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.FontSettings
import kotlin.math.roundToInt

private const val BASE_URL = "https://m3.material.io/styles/icons/applying-icons"
private const val FILL_HELP_URL = "$BASE_URL#ebb3ae7d-d274-4a25-9356-436e82084f1f"
private const val WEIGHT_HELP_URL = "$BASE_URL#d7f45762-67ac-473d-95b0-9214c791e242"
private const val GRADE_HELP_URL = "$BASE_URL#3ad55207-1cb0-43af-8092-fad2762f69f7"
private const val OPTICAL_SIZE_HELP_URL = "$BASE_URL#b41cbc01-9b49-4a44-a525-d153d1ea1425"

@Composable
fun FontCustomization(
    fontSettings: FontSettings,
    onSettingsChange: (FontSettings) -> Unit,
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
                text = "Customize",
                style = MaterialTheme.typography.titleMedium,
            )
            WeightSpacer()
            TextButton(
                onClick = {
                    onSettingsChange(FontSettings())
                },
                enabled = fontSettings.isModified,
            ) {
                Text(text = "Reset")
            }
            HorizontalSpacer(4.dp)
        }
        FontPlayground(
            fontSettings = fontSettings,
            onSettingsChange = onSettingsChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FontPlayground(
    fontSettings: FontSettings,
    onSettingsChange: (FontSettings) -> Unit,
) {
    val browser = rememberBrowser()

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        VerticalSpacer(8.dp)
        CenterVerticalRow {
            Text(text = "Fill", fontWeight = FontWeight.Medium)
            IconButtonSmall(
                imageVector = ValkyrieIcons.Filled.Help,
                iconSize = 20.dp,
                onClick = { browser.open(FILL_HELP_URL) },
            )
            WeightSpacer()
            Switch(
                checked = fontSettings.fill,
                onCheckedChange = { onSettingsChange(fontSettings.copy(fill = it)) },
            )
        }
        VerticalSpacer(32.dp)

        CenterVerticalRow {
            Text(text = "Weight", fontWeight = FontWeight.Medium)
            IconButtonSmall(
                imageVector = ValkyrieIcons.Filled.Help,
                iconSize = 20.dp,
                onClick = { browser.open(WEIGHT_HELP_URL) },
            )
        }
        SliderWithLabel(
            value = fontSettings.weight.toFloat(),
            onValueChange = { onSettingsChange(fontSettings.copy(weight = it.roundToInt())) },
            valueRange = 100f..700f,
            steps = 5,
            labelText = fontSettings.weight.toString(),
            minLabel = "100",
            maxLabel = "700",
        )
        VerticalSpacer(32.dp)

        CenterVerticalRow {
            Text(text = "Grade", fontWeight = FontWeight.Medium)
            IconButtonSmall(
                imageVector = ValkyrieIcons.Filled.Help,
                iconSize = 20.dp,
                onClick = { browser.open(GRADE_HELP_URL) },
            )
        }
        SliderWithLabel(
            value = when (fontSettings.grade) {
                -25 -> 0f
                0 -> 1f
                else -> 2f
            },
            onValueChange = { value ->
                val newGrade = when (value) {
                    0f -> -25f
                    1f -> 0f
                    else -> 200f
                }.toInt()
                onSettingsChange(fontSettings.copy(grade = newGrade))
            },
            valueRange = 0f..2f,
            steps = 1,
            labelText = fontSettings.grade.toString(),
            minLabel = "-25",
            maxLabel = "200",
        )
        VerticalSpacer(32.dp)

        CenterVerticalRow {
            Text(text = "Optical Size", fontWeight = FontWeight.Medium)
            IconButtonSmall(
                imageVector = ValkyrieIcons.Filled.Help,
                iconSize = 20.dp,
                onClick = { browser.open(OPTICAL_SIZE_HELP_URL) },
            )
        }
        SliderWithLabel(
            value = when (fontSettings.opticalSize.roundToInt()) {
                20 -> 0f
                24 -> 1f
                40 -> 2f
                else -> 3f
            },
            onValueChange = { value ->
                val newOpticalSize = when (value.roundToInt()) {
                    0 -> 20f
                    1 -> 24f
                    2 -> 40f
                    else -> 48f
                }
                onSettingsChange(fontSettings.copy(opticalSize = newOpticalSize))
            },
            valueRange = 0f..3f,
            steps = 2,
            labelText = "${fontSettings.opticalSize.roundToInt()}px",
            minLabel = "20px",
            maxLabel = "48px",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SliderWithLabel(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    labelText: String,
    minLabel: String,
    maxLabel: String,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = modifier) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            interactionSource = interactionSource,
            colors = SliderDefaults.colors().copy(
                inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            ),
            thumb = {
                Label(
                    label = {
                        PlainTooltip(
                            modifier = Modifier
                                .sizeIn(45.dp, 25.dp)
                                .width(IntrinsicSize.Min),
                            containerColor = MaterialTheme.colorScheme.primary,
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = labelText,
                                textAlign = TextAlign.Center,
                            )
                        }
                    },
                    interactionSource = interactionSource,
                ) {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        colors = SliderDefaults.colors().copy(thumbColor = MaterialTheme.colorScheme.primary),
                    )
                }
            },
        )
        CenterVerticalRow {
            Text(text = minLabel, style = MaterialTheme.typography.bodyMedium)
            WeightSpacer()
            Text(text = maxLabel, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview
@Composable
private fun FontCustomizationPreview() = PreviewTheme(alignment = Alignment.TopEnd) {
    FontCustomization(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        fontSettings = FontSettings(fill = true),
        onClose = {},
        onSettingsChange = { },
    )
}
